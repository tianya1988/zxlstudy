package kafka

import java.util

import kafka.api._
import kafka.client.ClientUtils
import kafka.cluster.BrokerEndPoint
import kafka.common.TopicAndPartition
import kafka.consumer._

/**
 * Created by jason on 17-3-31.
 */
object OffsetUtil {

  def getOffsetInfo(brokerStr: String, topic: String, offsetTime: Long): util.HashMap[Integer, Long] = {
    val topicSet: Set[String] = Set(topic)

    val brokerSeq: Seq[BrokerEndPoint] = ClientUtils.parseBrokerList(brokerStr)
    val topicsMetadata: Seq[TopicMetadata] = ClientUtils.fetchTopicMetadata(topicSet, brokerSeq, "GetOffsetUtil", 1000).topicsMetadata

    if (topicsMetadata.size != 1 || !topicsMetadata(0).topic.equals(topic)) {
      System.err.println(("Error: no valid topic metadata for topic: %s, " + " probably the topic does not exist, run ").format(topic) +
        "kafka-list-topic.sh to verify")
      System.exit(1)
    }

    val partitions: Seq[Int] = topicsMetadata.head.partitionsMetadata.map(_.partitionId)

    val clientId = "GetOffsetShell"

    val earliestOffsetMap: util.HashMap[Integer, Long] = new util.HashMap[Integer, Long]()
    val latestOffsetMap: util.HashMap[Integer, Long] = new util.HashMap[Integer, Long]()
    var offsetMap: util.HashMap[Integer, Long] = new util.HashMap[Integer, Long]()

    partitions.foreach(partitionId => {
      val partitionMetadataOpt: Option[PartitionMetadata] = topicsMetadata.head.partitionsMetadata.find(_.partitionId == partitionId)

      partitionMetadataOpt match {
        case Some(metadata) =>

          metadata.leader match {

            case Some(leader) =>
              val consumer = new SimpleConsumer(leader.host, leader.port, 10000, 100000, clientId)
              val topicAndPartition = TopicAndPartition(topic, partitionId)

              // offsetTime <Long: timestamp/-1(latest)/-2(earliest)  timestamp of the offsets before that
              // number of offsets returned (default: 1)

              if (offsetTime == -1 || offsetTime == -3) {
                val request: OffsetRequest = OffsetRequest.apply(Map(topicAndPartition -> PartitionOffsetRequestInfo(-1, 1)))
                val latestOffsets = consumer.getOffsetsBefore(request).partitionErrorAndOffsets(topicAndPartition).offsets
                latestOffsetMap.put(partitionId, latestOffsets(0))
              }

              if (offsetTime == -2 || offsetTime == -3) {
                val request2: OffsetRequest = OffsetRequest.apply(Map(topicAndPartition -> PartitionOffsetRequestInfo(-2, 1)))
                val earliestOffsets = consumer.getOffsetsBefore(request2).partitionErrorAndOffsets(topicAndPartition).offsets
                earliestOffsetMap.put(partitionId, earliestOffsets(0))
              }

              if (offsetTime == -1) {
                println("Get the latest offset => %s:%d:%s".format(topic, partitionId, latestOffsetMap.get(partitionId)))
              }
              if (offsetTime == -2) {
                println("Get the earliest offset => %s:%d:%s".format(topic, partitionId, earliestOffsetMap.get(partitionId)))
              }

              if (offsetTime == -3) {
                println("Get partition offset => %s:%d:%s:%s".format(topic, partitionId, earliestOffsetMap.get(partitionId), latestOffsetMap.get(partitionId)))
              }

            case None => System.err.println("Error: partition %d does not have a leader. Skip getting offsets".format(partitionId))
          }
        case None => System.err.println("Error: partition %d does not exist".format(partitionId))
      }

      if (offsetTime == -1) {
        offsetMap = latestOffsetMap;
      }
      if (offsetTime == -2) {
        offsetMap = earliestOffsetMap;
      }
    })

    return offsetMap
  }

  def main(args: Array[String]) {
//    getOffsetInfo("11.11.60.127:6667", "avro-cp-log-fw", -2)
//    println("=========================")
//    getOffsetInfo("11.11.60.127:6667", "avro-cp-log-fw", -1)

    getOffsetInfo("11.11.60.127:6667", "avro-cp-log-fw", -3)

  }


}
