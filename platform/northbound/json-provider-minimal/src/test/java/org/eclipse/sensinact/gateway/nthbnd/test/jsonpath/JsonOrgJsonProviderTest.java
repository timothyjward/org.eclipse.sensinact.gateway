package org.eclipse.sensinact.gateway.nthbnd.test.jsonpath;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Test;

import java.util.List;
import java.util.Map;

import static com.jayway.jsonpath.JsonPath.using;
import static org.assertj.core.api.Assertions.assertThat;

public class JsonOrgJsonProviderTest extends BaseTestConfiguration {


    @Test
    public void an_object_can_be_read() throws JSONException {

        JSONObject book = using(JSON_ORG_CONFIGURATION).parse(BaseTestJson.JSON_DOCUMENT, false).read("$.store.book[0]");

        assertThat(book.get("author").toString()).isEqualTo("Nigel Rees");
    }

    @Test
    public void a_property_can_be_read() {

        String category = using(JSON_ORG_CONFIGURATION).parse(BaseTestJson.JSON_DOCUMENT, false).read("$.store.book[0].category");

        assertThat(category).isEqualTo("reference");
    }

    @Test
    public void a_filter_can_be_applied() {

        JSONArray fictionBooks = using(JSON_ORG_CONFIGURATION).parse(BaseTestJson.JSON_DOCUMENT, false).read("$.store.book[?(@.category == 'fiction')]");

        assertThat(fictionBooks.length()).isEqualTo(3);
    }

    @Test
    public void result_can_be_mapped_to_object() {

        List<Map<String, Object>> books = using(JSON_ORG_CONFIGURATION).parse(BaseTestJson.JSON_DOCUMENT, false).read("$.store.book", List.class);

        assertThat(books.size()).isEqualTo(4);
    }

    @Test
    public void read_books_with_isbn() {

        JSONArray books = using(JSON_ORG_CONFIGURATION).parse(BaseTestJson.JSON_DOCUMENT, false).read("$..book[?(@.isbn)]");

        assertThat(books.length()).isEqualTo(2);
    }
}