/*
package es.client

import java.net.InetAddress

import com.alibaba.fastjson.JSON
import es.bean.ElasticRecord
import org.elasticsearch.action.bulk.BulkRequestBuilder
import org.elasticsearch.action.index.IndexRequest
import org.elasticsearch.action.support.WriteRequest.RefreshPolicy
import org.elasticsearch.client.transport.{NoNodeAvailableException, TransportClient}
import org.elasticsearch.common.settings.Settings
import org.elasticsearch.common.transport.InetSocketTransportAddress
import org.elasticsearch.transport.client.PreBuiltTransportClient
import org.junit.Test

/**
 * Created by h on 15-12-27.
 */
class ElasticsearchTemplateTest {
  @Test
  def testAddAllByTimeField: Unit = {

    val settings = Settings.builder()
      .put("cluster.name", "kf01")
      .build()
    val client: TransportClient = new PreBuiltTransportClient(settings)
    client.addTransportAddress(new InetSocketTransportAddress(InetAddress.getByName("11.11.165.61"), 9300))

    val s = "{\"myId\":123,\"userName\":\"myName\"}"
    val json = JSON.parseObject(s)
    val record = new ElasticRecord()
    record.setData(json)

    val bulkRequest = client.prepareBulk()
    val indexRequest: IndexRequest = new IndexRequest("test-index-", "test-type")
    indexRequest.source(record.getData())
    bulkRequest.add(indexRequest)

    execute(bulkRequest, false, false)
  }

  def execute(bulkRequest: BulkRequestBuilder, throwException: Boolean, refresh: Boolean): Unit = {
    val begin = System.currentTimeMillis()
    val recordSize = bulkRequest.numberOfActions()
    try {
      bulkRequest.setRefreshPolicy(RefreshPolicy.NONE)
      val response = bulkRequest.execute().actionGet()
      if (response.hasFailures()) {
        if (throwException) {
          throw new IllegalArgumentException(response.buildFailureMessage())
        } else {
          println(response.buildFailureMessage())
          println(s"bulk failure message, lost size is ${response.getItems().size}")
        }
      } else {
        println(s"Thread is ${Thread.currentThread().getName}, save to es success, batch commit $recordSize items, took " + (System.currentTimeMillis() - begin) + " ms")
      }
    } catch {
      case e: NoNodeAvailableException =>
        if (throwException) {
          throw e
        } else {
          e.printStackTrace(Console.out)
          println(s"data save to es error, no available elasticsearch node!, lost size is $recordSize")
        }
      case e: Exception => {
        if (throwException) {
          throw e
        } else {
          println(s"The data insert into ElasticSearch have some errors, lost size is $recordSize, the detail is :", e)
        }
      }
    }
  }

}
*/
