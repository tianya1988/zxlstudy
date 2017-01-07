package es;

import com.google.gson.Gson;
import es.bean.EsBean;
import es.bean.EsStrToBean;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;

import java.io.IOException;
import java.text.ParseException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static org.elasticsearch.common.xcontent.XContentFactory.jsonBuilder;

/**
 * Created by jason on 15-9-20.
 */
public class EsData {

    public static void main(String[] args) throws ParseException, InterruptedException, ExecutionException, IOException {

        Client client = new TransportClient().addTransportAddress(new InetSocketTransportAddress("localhost", 9300));

        String esIndex = "my_es_index";
        String esType = "my_es_type";
        String esId = "my_es_id_6";

//        createIndex(client, esIndex, esType, esId);
        getDataFromEs(client, esIndex, esType, esId);
//        searchDataFromEs(client, esIndex, esType, "beanName", "zhangxuelongEsBean");
//        updateDataFromEs(client, esIndex, esType, esId);
//        deleteDataFromEs(client, esIndex, esType, esId);


    }

    private static void deleteDataFromEs(Client client, String esIndex, String esType, String esId) {
        DeleteResponse response = client.prepareDelete(esIndex, esType, esId).execute().actionGet();
        System.out.println();
    }

    private static void updateDataFromEs(Client client, String esIndex, String esType, String esId) throws IOException, ExecutionException, InterruptedException {
        System.out.println("-------- update data from es ---------");
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(esIndex);
        updateRequest.type(esType);
        updateRequest.id(esId);
        updateRequest.doc(jsonBuilder()
                .startObject()
                .field("beanName", "updateTest")
                .endObject());

        client.update(updateRequest).get();
    }

    private static void getDataFromEs(Client client, String esIndex, String esType, String esId) {
        System.out.println("-------- get data from es ---------");

        GetResponse response = client.prepareGet(esIndex, esType, esId).execute().actionGet();

        System.out.println("source: " + response.getSource());
        System.out.println("index: " + response.getIndex());
        System.out.println("type: " + response.getType());
        System.out.println("id: " + response.getId());
        System.out.println("version: " + response.getVersion());
        System.out.println();

        String esSourceStr = response.getSourceAsString();
        System.out.println(esSourceStr);

        Gson gson = new Gson();
        EsStrToBean esBean = gson.fromJson(esSourceStr, EsStrToBean.class);
        System.out.println(esBean.getBeanId());
        System.out.println(esBean.getBeanName());
        System.out.println(esBean.getBeanCreateTime());


    }

    private static void createIndex(Client client, String esIndex, String esType, String esId) throws ParseException {
        System.out.println("-------- create es index ---------");
        EsBean esBean = new EsBean();
        esBean.setId(6);
        esBean.setName("zhangxuelongEsBean5");
//        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//        esBean.setCreateTime(sdf.parse(sdf.format(new Date())));
//        esBean.setCreateTime(new Date());

        System.out.println(esBean.getCreateTime());

        Map<String, Object> esBeanMap = new HashMap<String, Object>();
//        esBeanMap.put("myEsBean", esBean);
        esBeanMap.put("beanId", esBean.getId());
        esBeanMap.put("beanName", esBean.getName());
        esBeanMap.put("beanCreateTime", esBean.getCreateTime());

        //setSource()-- the param must be a map
        client.prepareIndex(esIndex, esType, esId).setSource(esBeanMap).execute().actionGet();
        System.out.println();
    }

    private static void searchDataFromEs(Client client, String esIndex, String esType, String beanName, String value) {
        //fuzzyQuery but failure
        SearchResponse response = client.prepareSearch(esIndex)
                .setTypes(esType)
                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.fuzzyQuery(beanName, value))
                .setFrom(0).setSize(2).setExplain(true)
                .execute()
                .actionGet();

        // Precise query
//        SearchResponse response = client.prepareSearch(esIndex)
//                .setTypes(esType)
//                .setSearchType(SearchType.DFS_QUERY_AND_FETCH).setQuery(QueryBuilders.matchQuery(beanName, value))
//                .setFrom(0).setSize(2).setExplain(true)
//                .execute()
//                .actionGet();

        //MatchAll on the whole cluster with all default options
//        SearchResponse response = client.prepareSearch().execute().actionGet();

        SearchHit[] results = response.getHits().getHits();
        System.out.println("Current results: " + results.length);
        for (SearchHit hit : results) {
            Map<String,Object> result = hit.getSource();
            System.out.println(result);
        }

        System.out.println();
    }
}
