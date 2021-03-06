/*
* Copyright (c) 2020 Kentyou.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
*    Kentyou - initial API and implementation
 */
package org.eclipse.sensinact.gateway.core.security.impl;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import org.eclipse.sensinact.gateway.common.bundle.Mediator;
import org.eclipse.sensinact.gateway.core.method.AccessMethod;
import org.eclipse.sensinact.gateway.core.security.AccessLevelOption;
import org.eclipse.sensinact.gateway.core.security.AccessNode;
import org.eclipse.sensinact.gateway.core.security.AccessNodeImpl;
import org.eclipse.sensinact.gateway.core.security.AccessProfileImpl;
import org.eclipse.sensinact.gateway.core.security.AccessProfileOption;
import org.eclipse.sensinact.gateway.core.security.AccessTree;
import org.eclipse.sensinact.gateway.core.security.AccessTreeImpl;
import org.eclipse.sensinact.gateway.core.security.AuthorizationService;
import org.eclipse.sensinact.gateway.core.security.ImmutableAccessNode;
import org.eclipse.sensinact.gateway.core.security.ImmutableAccessTree;
import org.eclipse.sensinact.gateway.core.security.MethodAccess;
import org.eclipse.sensinact.gateway.core.security.MethodAccessImpl;
import org.eclipse.sensinact.gateway.core.security.MutableAccessNode;
import org.eclipse.sensinact.gateway.core.security.MutableAccessTree;
import org.eclipse.sensinact.gateway.core.security.SecuredAccess;
import org.eclipse.sensinact.gateway.core.security.SecuredAccessException;
import org.eclipse.sensinact.gateway.core.security.SecurityDataStoreService;
import org.eclipse.sensinact.gateway.core.security.UserManager;
import org.eclipse.sensinact.gateway.core.security.dao.AgentDAO;
import org.eclipse.sensinact.gateway.core.security.dao.ApplicationDAO;
import org.eclipse.sensinact.gateway.core.security.dao.AuthenticatedAccessLevelDAO;
import org.eclipse.sensinact.gateway.core.security.dao.BundleDAO;
import org.eclipse.sensinact.gateway.core.security.dao.DAOException;
import org.eclipse.sensinact.gateway.core.security.dao.ObjectDAO;
import org.eclipse.sensinact.gateway.core.security.dao.ObjectProfileAccessDAO;
import org.eclipse.sensinact.gateway.core.security.entity.AgentEntity;
import org.eclipse.sensinact.gateway.core.security.entity.ApplicationEntity;
import org.eclipse.sensinact.gateway.core.security.entity.AuthenticatedAccessLevelEntity;
import org.eclipse.sensinact.gateway.core.security.entity.BundleEntity;
import org.eclipse.sensinact.gateway.core.security.entity.ObjectEntity;
import org.eclipse.sensinact.gateway.datastore.api.DataStoreException;
import org.eclipse.sensinact.gateway.util.UriUtils;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Secured access service implementation
 * 
 * @author <a href="mailto:christophe.munilla@cea.fr">Christophe Munilla</a>
 */
public class SecuredAccessImpl implements SecuredAccess {
	// ********************************************************************//
	// NESTED DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// ABSTRACT DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// STATIC DECLARATIONS //
	// ********************************************************************//

	// ********************************************************************//
	// INSTANCE DECLARATIONS //
	// ********************************************************************//

	private final Mediator mediator;

	private BundleDAO bundleDAO;
	private AgentDAO agentDAO;
	private ApplicationDAO applicationDAO;
	private ObjectDAO objectDAO;
	private ObjectProfileAccessDAO objectProfileAccessDAO;
	private AuthenticatedAccessLevelDAO authenticatedAccessLevelDAO;

	private ObjectEntity root;
	private AccessProfileOption rootObjectProfileOption;

	private ServiceRegistration<AuthorizationService> authorizationRegistration;

	/**
	 * Constructor
	 * 
	 * @param mediator
	 *            the {@link Mediator} allowing to interact with the OSGi host 
	 *            environment
	 * @throws DataStoreException
	 */
	public SecuredAccessImpl(Mediator mediator) throws SecuredAccessException {
		this.mediator = mediator;
		try {
			ServiceReference<SecurityDataStoreService> reference = this.mediator.getContext()
					.getServiceReference(SecurityDataStoreService.class);

			SecurityDataStoreService dataStoreService = this.mediator.getContext().getService(reference);

			this.applicationDAO = new ApplicationDAO(mediator, dataStoreService);
			this.agentDAO = new AgentDAO(mediator, dataStoreService);
			this.objectDAO = new ObjectDAO(mediator, dataStoreService);
			this.bundleDAO = new BundleDAO(mediator, dataStoreService);
			this.objectProfileAccessDAO = new ObjectProfileAccessDAO(mediator, dataStoreService);
			this.authenticatedAccessLevelDAO = new AuthenticatedAccessLevelDAO(mediator, dataStoreService);

			root = this.objectDAO.select(new HashMap<String, Object>() {
				{
					this.put("OID", 0l);

				}
			}).get(0);

			rootObjectProfileOption = this.objectProfileAccessDAO.getAccessProfileOption(root.getObjectProfileEntity());
		} catch (DAOException | DataStoreException e) {
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @inheritDoc
	 *
	 * @see SecuredAccess#buildAccessNodesHierarchy(String, String, AccessTree)
	 */
	@Override
	public void buildAccessNodesHierarchy(String signature, String name,
			MutableAccessTree<? extends MutableAccessNode> tree) throws SecuredAccessException {
		try {
			if (name == null) {
				throw new NullPointerException("The sensiNact resource model's name is missing");
			}
			if (!checkIdentifier(signature, name)) {
				if (signature == null) {
					throw new SecuredAccessException(
							String.format("A '%s' sensiNact resource model already exists in the data store", name));
				} else {
					throw new SecuredAccessException("Invalid bundle identifier");
				}
			} else if (signature != null) {
				buildNodes(tree, this.objectDAO.find(UriUtils.getUri(new String[] { name })));
			}
		} catch (DAOException e) {
			throw new SecuredAccessException(e);

		} catch (Exception e) {
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @inheritDoc
	 *
	 * @see org.eclipse.sensinact.gateway.core.security.SecuredAccess#
	 *      getAccessTree(java.lang.String)
	 */
	@Override
	public MutableAccessTree<? extends MutableAccessNode> getAccessTree(String signature)
			throws SecuredAccessException {
		MutableAccessTree<? extends AccessNodeImpl<?>> tree = null;
		BundleEntity object = null;
		AccessProfileOption option = null;
		try {
			if (signature == null || (object = this.bundleDAO.find(signature)) == null) {
				option = rootObjectProfileOption;

			} else {
				option = this.objectProfileAccessDAO.getAccessProfileOption(object.getObjectProfileEntity());
			}
			tree = new AccessTreeImpl<>(mediator).withAccessProfile(option);

		} catch (Exception e) {
			throw new SecuredAccessException(e);
		}
		return tree;
	}

	/**
	 * @inheritDoc
	 *
	 * @see SecuredAccess#getRootNode(java.lang.String)
	 */
	@Override
	public AccessTree<? extends AccessNode> getUserAccessTree(String publicKey) throws SecuredAccessException {
		AccessTreeImpl<? extends AccessNodeImpl<?>> tree = null;
		AccessLevelOption option = null;
		try {
			// User user = this.userManager.getUserFromPublicKey(publicKey);
			Map<String, Object> directive = new HashMap<String, Object>();
			directive.put("UOID", 0L);
			directive.put("PUBLIC_KEY", publicKey);// user.getPublicKey());

			List<AuthenticatedAccessLevelEntity> entities = this.authenticatedAccessLevelDAO.select(directive);

			if (entities == null || entities.size() != 1) {
				option = AccessLevelOption.ANONYMOUS;

			} else {
				option = entities.get(0).getAccessLevelOption();
			}
			AccessMethod.Type[] types = AccessMethod.Type.values();
			int length = types == null ? 0 : types.length;

			Set<MethodAccess> methodAccesses = new HashSet<MethodAccess>();
			for (int index = 0; index < length; index++) {
				methodAccesses.add(new MethodAccessImpl(option.getAccessLevel(), types[index]));
			}
			tree = new AccessTreeImpl<>(mediator);
			tree.getRoot().withAccessProfile(new AccessProfileImpl(methodAccesses));
			this.buildTree(tree, publicKey);// user);

			return tree.<ImmutableAccessNode, ImmutableAccessTree>immutable(ImmutableAccessTree.class,
					ImmutableAccessNode.class);

		} catch (Exception e) {
			e.printStackTrace();
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @param tree
	 * @param object
	 * @throws Exception
	 */
	private void buildTree(MutableAccessTree<? extends MutableAccessNode> tree, String publicKey)// User user)
			throws SecuredAccessException {
		// if (user == null || user.getPublicKey() == null ||user.isAnonymous())
		// {
		// return;
		// }
		if (publicKey == null || publicKey.startsWith(UserManager.ANONYMOUS_PKEY)) {
			return;
		}
		try {
			// we have to retrieve all the objects for which the user has been
			// attached with a specific access level.
			Map<String, Object> directive = new HashMap<String, Object>();
			directive.put("PUBLIC_KEY", publicKey);

			List<AuthenticatedAccessLevelEntity> entities = this.authenticatedAccessLevelDAO.select(directive);

			Iterator<AuthenticatedAccessLevelEntity> iterator = entities.iterator();
			directive.clear();

			AccessMethod.Type[] types = AccessMethod.Type.values();
			int length = types == null ? 0 : types.length;
			int index;

			while (iterator.hasNext()) {
				AuthenticatedAccessLevelEntity entity = iterator.next();
				AccessLevelOption option = entity.getAccessLevelOption();
				long objectId = entity.getObjectId();
				if (objectId == 0L) {
					continue;
				}
				directive.put("OID", objectId);
				List<ObjectEntity> objectEntities = objectDAO.select(directive);
				if (objectEntities == null || objectEntities.size() != 1) {
					continue;
				}
				Set<MethodAccess> methodAccesses = new HashSet<MethodAccess>();
				for (index = 0; index < length; index++) {
					methodAccesses.add(new MethodAccessImpl(option.getAccessLevel(), types[index]));
				}
				Stack<ObjectEntity> family = new Stack<ObjectEntity>();
				ObjectEntity familyMember = objectEntities.get(0);
				while (familyMember != null && familyMember.getIdentifier() != 0) {
					family.push(familyMember);
					directive.clear();
					directive.put("OID", familyMember.getParent());
					objectEntities = objectDAO.select(directive);
					familyMember = objectEntities.isEmpty() ? null : objectEntities.get(0);
				}
				MutableAccessNode node = null;
				while (!family.isEmpty()) {
					familyMember = family.pop();
					node = tree.add(familyMember.getPath(), familyMember.isPattern());
				}
				node.withAccessProfile(new AccessProfileImpl(methodAccesses));
			}
			// we also have to retrieve all the objects that does not appear in the
			// previous list (attached to the user) and for which the SAUTH field (
			// users known by the system are defined as authenticated) has been
			// defined to true (1)

			// TODO : check whether this is already handled by the database (I think so)
			// before deleting - If it is the case a factorization will be possible between
			// the different methods returning an AccessTree according to a String public
			// key
			// parameter

			// directive.clear();
			// directive.put("SAUTH", 1);
			// List<ObjectEntity> objectEntities = objectDAO.select(directive);
			// if(objectEntities==null || objectEntities.isEmpty())
			// {
			// return;
			// }
			// AccessLevelOption option = AccessLevelOption.AUTHENTICATED;
			// Set<MethodAccess> methodAccesses = new HashSet<MethodAccess>();
			// for(index = 0;index < length;index++)
			// {
			// methodAccesses.add(new MethodAccessImpl(
			// option.getAccessLevel(), types[index]));
			// }
			// Iterator<ObjectEntity> objectIterator = objectEntities.iterator();
			// while(iterator.hasNext())
			// {
			// ObjectEntity objectEntity = objectIterator.next();
			// tree.add(objectEntity.getPath()).withAccessProfile(
			// new AccessProfileImpl(methodAccesses));
			// }
		} catch (Exception e) {
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @inheritDoc
	 *
	 * @see org.eclipse.sensinact.gateway.core.security.SecuredAccess#
	 *      getApplicationAccessTree(java.lang.String)
	 */
	@Override
	public AccessTree<? extends AccessNode> getApplicationAccessTree(String publicKey) throws SecuredAccessException {
		AccessTreeImpl<? extends AccessNodeImpl<?>> tree = null;
		AccessLevelOption option = null;
		try {
			Map<String, Object> directive = new HashMap<String, Object>();
			directive.put("UOID", 0L);
			directive.put("PUBLIC_KEY", publicKey == null ? UserManager.ANONYMOUS_PKEY : publicKey);

			List<AuthenticatedAccessLevelEntity> entities = this.authenticatedAccessLevelDAO.select(directive);

			if (entities == null || entities.size() != 1) {
				option = AccessLevelOption.ANONYMOUS;

			} else {
				option = entities.get(0).getAccessLevelOption();
			}
			AccessMethod.Type[] types = AccessMethod.Type.values();
			int length = types == null ? 0 : types.length;

			Set<MethodAccess> methodAccesses = new HashSet<MethodAccess>();
			for (int index = 0; index < length; index++) {
				methodAccesses.add(new MethodAccessImpl(option.getAccessLevel(), types[index]));
			}
			tree = new AccessTreeImpl<>(mediator);
			tree.getRoot().withAccessProfile(new AccessProfileImpl(methodAccesses));
			this.buildTree(tree, // this.applicationDAO.findFromPublicKey(
					publicKey);// );

		} catch (Exception e) {
			throw new SecuredAccessException(e);
		}
		return tree.<ImmutableAccessNode, ImmutableAccessTree>immutable(ImmutableAccessTree.class,
				ImmutableAccessNode.class);
	}

	// /**
	// * @param tree
	// * @param application
	// * @throws SecuredAccessException
	// */
	// private void buildTree( MutableAccessTree<? extends MutableAccessNode> tree,
	// ApplicationEntity application) throws SecuredAccessException
	// {
	// if (application == null
	// || application.getPublicKey() == null
	// || application.getPublicKey().equals(
	// UserManager.ANONYMOUS_PKEY))
	// {
	// return;
	// }
	// try
	// {
	// //we have to retrieve all the objects for which the user has been
	// //attached with a specific access level.
	// Map<String,Object> directive = new HashMap<String,Object>();
	// directive.put("PUBLIC_KEY", application.getPublicKey());
	//
	// List<AuthenticatedAccessLevelEntity> entities =
	// this.authenticatedAccessLevelDAO.select(directive);
	//
	// Iterator<AuthenticatedAccessLevelEntity> iterator = entities.iterator();
	// directive.clear();
	//
	// AccessMethod.Type[] types = AccessMethod.Type.values();
	// int length = types==null?0:types.length;
	// int index;
	//
	// while(iterator.hasNext())
	// {
	// AuthenticatedAccessLevelEntity entity = iterator.next();
	// AccessLevelOption option = entity.getAccessLevelOption();
	// long objectId = entity.getObjectId();
	// if(objectId == 0L)
	// {
	// continue;
	// }
	// directive.put("OID", objectId);
	// List<ObjectEntity> objectEntities = objectDAO.select(directive);
	// if(objectEntities==null || objectEntities.size()!=1)
	// {
	// continue;
	// }
	// Set<MethodAccess> methodAccesses = new HashSet<MethodAccess>();
	// for(index = 0;index < length;index++)
	// {
	// methodAccesses.add(new MethodAccessImpl(
	// option.getAccessLevel(), types[index]));
	// }
	//
	// Stack<ObjectEntity> family = new Stack<ObjectEntity>();
	// ObjectEntity familyMember = objectEntities.get(0);
	// while(familyMember!=null && familyMember.getIdentifier() != 0)
	// {
	// family.push(familyMember);
	// directive.clear();
	// directive.put("OID", familyMember.getParent());
	// objectEntities = objectDAO.select(directive);
	// familyMember = objectEntities.isEmpty()?null:objectEntities.get(0);
	// }
	// MutableAccessNode node = null;
	// while(!family.isEmpty())
	// {
	// familyMember = family.pop();
	// node = tree.add(familyMember.getPath(),familyMember.isPattern());
	// }
	// node.withAccessProfile(new AccessProfileImpl(methodAccesses));
	// }
	// } catch (Exception e)
	// {
	// throw new SecuredAccessException(e);
	// }
	// }

	/**
	 * @param signature
	 * @param name
	 * @return
	 * @throws SecuredAccessException
	 */
	private boolean checkIdentifier(String signature, String name) throws SecuredAccessException {
		try {
			if (name == null) {
				return false;
			}
			List<ObjectEntity> entities = this.objectDAO.find(UriUtils.getUri(new String[] { name }), true);

			if (entities.size() == 0) {
				return true;
			}
			BundleEntity bundle = this.bundleDAO.find(signature);
			if (bundle == null) {
				return false;
			}
			while (!entities.isEmpty()) {
				ObjectEntity entity = entities.remove(0);
				if (bundle.getIdentifier() == entity.getBundleEntity()) {
					return true;
				}
			}
			return false;

		} catch (Exception e) {
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @param tree
	 * @param list
	 * @throws Exception
	 */
	private void buildNodes(MutableAccessTree<? extends MutableAccessNode> tree, List<ObjectEntity> objectEntities)
			throws SecuredAccessException {

		if (objectEntities == null || objectEntities.isEmpty()) {
			return;
		}
		while (!objectEntities.isEmpty()) {
			ObjectEntity objectEntity = objectEntities.remove(0);
			if (objectEntity.getPath() == null) {
				continue;
			}
			try {
				AccessProfileOption option = this.objectProfileAccessDAO
						.getAccessProfileOption(objectEntity.getObjectProfileEntity());

				tree.add(objectEntity.getPath(), objectEntity.isPattern()).withAccessProfile(option);

				buildNodes(tree, this.objectDAO.findChildren(objectEntity.getIdentifier()));

			} catch (Exception e) {
				throw new SecuredAccessException(e);
			}
		}
	}

	/**
	 * @inheritDoc
	 *
	 * @see SecuredAccess#createAuthorizationService()
	 * 
	 * @throws SecuredAccessException
	 */
	@Override
	public void createAuthorizationService() throws SecuredAccessException {
		try {
			this.authorizationRegistration = this.mediator.getContext().registerService(AuthorizationService.class,
					new AuthorizationServiceImpl(mediator, this.authenticatedAccessLevelDAO), null);
		} catch (DAOException e) {
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @throws DataStoreException
	 * @inheritDoc
	 *
	 * @see org.eclipse.sensinact.gateway.core.security.SecuredAccess#
	 *      getAgentPublicKey(java.lang.String)
	 */
	@Override
	public String getAgentPublicKey(String signature) throws SecuredAccessException, DataStoreException {
		try {
			String agentKey = null;
			AgentEntity entity = this.agentDAO.findFromBundle(signature);
			if (entity != null) {
				agentKey = entity.getPublicKey();
			}
			return agentKey;
		} catch (DAOException e) {
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @inheritDoc
	 *
	 * @see org.eclipse.sensinact.gateway.core.security.SecuredAccess#
	 *      getApplicationPublicKey(java.lang.String)
	 */
	@Override
	public String getApplicationPublicKey(String privateKey) throws SecuredAccessException {
		try {
			String publicKey = null;
			ApplicationEntity entity = this.applicationDAO.findFromPrivateKey(privateKey);
			if (entity != null) {
				publicKey = entity.getPublicKey();
			}
			return publicKey;
		} catch (DAOException | DataStoreException e) {
			throw new SecuredAccessException(e);
		}
	}

	/**
	 * @inheritDoc
	 *
	 * @see SecuredAccess#close()
	 */
	public void close() {
		if (mediator.isDebugLoggable()) {
			mediator.debug("closing sensiNact secured access");
		}
		if (this.authorizationRegistration != null) {
			try {
				this.authorizationRegistration.unregister();

			} catch (IllegalStateException e) {
				try {
					mediator.debug(e.getMessage());
				} catch (IllegalStateException ise) {
					// do nothing because it probably means
					// that the OSGi environment is closing
				}
			}
		}
	}
}