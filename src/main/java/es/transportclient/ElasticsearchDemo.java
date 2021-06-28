/*
package es.transportclient;

import com.alibaba.fastjson.JSONObject;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

*/
/**
 * Created by jason on 18-3-11.
 *//*

public class ElasticsearchDemo {
    public static void main(String[] args) throws Exception {
        //add();
        //bulkAdd();
        //MutiSearch();
        //delete();
        //deleteData();
    }

    private static void deleteData() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "zxl").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "lisi");
        jsonObject.put("age", 200);
        jsonObject.put("ip", "127.0.0.111");

        UpdateResponse updateResponse = client.prepareUpdate("test-my-index", "test-my-type", "11").setDoc(jsonObject).get();
    }

    private static void delete() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "zxl").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301));

        DeleteResponse response = client.prepareDelete("test-my-index", "test-my-type", "10").get();
    }

    private static void MutiSearch() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "zxl").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301));


        SearchRequestBuilder srb1 = client
                .prepareSearch("test-my-index").setQuery(QueryBuilders.queryStringQuery("zhangsan")).setSize(8);
        SearchRequestBuilder srb2 = client
                .prepareSearch("test-my-index").setQuery(QueryBuilders.matchQuery("name", "hao")).setSize(8);

        MultiSearchResponse sr = client.prepareMultiSearch()
//                .add(srb1)
                .add(srb2)
                .get();

        long nbHits = 0;
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            SearchHits hits = response.getHits();
            for (SearchHit hit : hits.getHits()) {
                System.out.println(hit.getSourceAsString());
            }
            nbHits += response.getHits().getTotalHits();
        }
        System.out.println(nbHits);
    }

    private static void bulkAdd() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "zxl").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301));

        BulkRequestBuilder bulkRequest = client.prepareBulk();
        //for =================
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("name", "zhangsan1");
        jsonObject1.put("age", 20);
        jsonObject1.put("ip", "127.0.0.1");

        JSONObject jsonObject2 = new JSONObject();
        jsonObject2.put("name", "zhangsan2");
        jsonObject2.put("age", 30);
        jsonObject2.put("ip", "127.0.0.2");

        bulkRequest.add(client.prepareIndex("test-my-index", "test-my-type", "10").setSource(jsonObject1));
        bulkRequest.add(client.prepareIndex("test-my-index", "test-my-type", "11").setSource(jsonObject2));
        //for=====================



        BulkResponse bulkResponse = bulkRequest.get();
        if (bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            bulkResponse.buildFailureMessage();
        }

        client.close();
    }

    private static void add() throws UnknownHostException {
        Settings settings = Settings.builder()
                .put("cluster.name", "zxl").build();
        TransportClient client = new PreBuiltTransportClient(settings)
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9300))
                .addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("localhost"), 9301));

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "zhangsan hao test");
        jsonObject.put("age", 20);
        jsonObject.put("ip", "127.0.0.1");


        client.prepareIndex("test-my-index", "test-my-type").setSource(jsonObject).get();

        client.close();
    }
}
*/
