package es.cvs2es;


import com.alibaba.fastjson.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.client.indices.CreateIndexRequest;
import org.elasticsearch.client.indices.CreateIndexResponse;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


/**
 * Created by jason on 21-6-22.
 */
public class ESProcessor {

    private static final Logger logger = LoggerFactory.getLogger(ESProcessor.class);

    private volatile static RestHighLevelClient client;
    private volatile static BulkProcessor bulkProcessor;

    private ESProcessor() {
    }

    public static RestHighLevelClient getClient() {
        if (client == null) {
            synchronized (RestHighLevelClient.class) {
                if (client == null) {
                    client = new RestHighLevelClient(RestClient.builder(
                            new HttpHost("172.16.3.97", 9200, "http")
                    ));// 初始化
                }
            }
        }
        return client;
    }

    /**
     * 创建bulkProcessor并初始化
     */
    public static BulkProcessor getProcessor() {
        if (bulkProcessor == null) {
            synchronized (BulkProcessor.class) {
                if (bulkProcessor == null) {
                    try {
                        BulkProcessor.Listener listener = new BulkProcessor.Listener() {
                            @Override
                            public void beforeBulk(long executionId, BulkRequest request) {
                                int numberOfActions = request.numberOfActions();
                                logger.info("Executing bulk [{}] with {} requests",
                                        executionId, numberOfActions);
                            }

                            @Override
                            public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                                if (response.hasFailures()) {
                                    logger.error("Bulk [{}] executed with failures", executionId);
                                } else {
                                    logger.info("Bulk [{}] completed in {} milliseconds",
                                            executionId, response.getTook().getMillis());
                                }
                            }

                            @Override
                            public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                                logger.error("Failed to execute bulk", failure);
                                System.out.println("Bulk is unsuccess : " + failure
                                        + ", executionId: " + executionId);
                            }
                        };

                        BulkProcessor.Builder builder = BulkProcessor.builder(
                                (request, bulkListener) -> getClient().bulkAsync(request, RequestOptions.DEFAULT, bulkListener),
                                listener, "scsc-bulk-processor");

                        builder.setBulkActions(500);//每添加500个request，执行一次bulk操作;(defaults to 1000, use -1 to disable it)
                        builder.setBulkSize(new ByteSizeValue(5L, ByteSizeUnit.MB));//每达到5M的请求size时，执行一次bulk操作
                        builder.setConcurrentRequests(0);//Set the number of concurrent requests allowed to be executed (default to 1, use 0 to only allow the execution of a single request)
                        builder.setFlushInterval(TimeValue.timeValueSeconds(10L));
                        builder.setBackoffPolicy(BackoffPolicy
                                .constantBackoff(TimeValue.timeValueSeconds(2L), 3));//set a constant back off policy that initially waits for 1 second and retries up to 3 times.

                        bulkProcessor = builder.build();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return bulkProcessor;
    }

    /**
     * 创建索引
     *
     * @param indexName
     * @throws IOException
     */
    private static void createIndex(String indexName) throws IOException {
        CreateIndexRequest request = new CreateIndexRequest(indexName);
        request.settings(Settings.builder()
                .put("index.number_of_shards", 3)
                .put("index.number_of_replicas", 2)
                .put("index.refresh_interval", "-1")
        );

        ActionListener<CreateIndexResponse> listener =
                new ActionListener<CreateIndexResponse>() {
                    @Override
                    public void onResponse(CreateIndexResponse createIndexResponse) {
                        System.out.println("Create index " + createIndexResponse.index() + " success !");
                    }

                    @Override
                    public void onFailure(Exception e) {
                        System.out.println("Create index failure !");
                    }
                };
        getClient().indices().createAsync(request, RequestOptions.DEFAULT, listener);
        //createAsync方法之后不能直接调用client.close方法
        //esp.client.close();
    }

    public static void indexData(String indexName, JSONObject jsonObject) {
        IndexRequest indexRequest = new IndexRequest(indexName);

//        indexRequest.id("4");

        indexRequest.source(jsonObject.toJSONString(), XContentType.JSON);

        BulkProcessor bulkProcessor = getProcessor();
        bulkProcessor.add(indexRequest);

/*        // TODO bulkProcessor 执行提交操作
        bulkProcessor.flush();
        closeProcessor();*/

        /*
        BulkRequest request = new BulkRequest();
        request.add(indexRequest);
        ActionListener<BulkResponse> listener = getBulkResponseActionListener();
        //简单
        client.bulkAsync(request, RequestOptions.DEFAULT, listener);
        */
    }

    public ActionListener<BulkResponse> getBulkResponseActionListener() {
        ActionListener<BulkResponse> listener = new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkResponse) {
            }

            @Override
            public void onFailure(Exception e) {
            }
        };
        return listener;
    }

    public static void closeProcessor() {
        try {
            getProcessor().flush();
            getProcessor().awaitClose(30L, TimeUnit.SECONDS);// The method returns true if all bulk requests completed and false if the waiting time elapsed before all the bulk requests completed
            getClient().close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException {
        String indexName = "scsc-test5";
//        createIndex(indexName);
        String jsonString = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch no id 1\"" +
                "}";

        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        indexData(indexName, jsonObject);
    }
}

