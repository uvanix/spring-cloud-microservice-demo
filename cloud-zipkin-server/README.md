## 分布式链路跟踪 ZipKin Server

### 简介
Zipkin是一个开放源代码分布式的跟踪系统，
由Twitter公司开源，它致力于收集服务的定时数据，以解决微服务架构中的延时问题，包括数据的收集、存储、查找和展现。
每个服务向zipkin报告计时数据，zipkin会根据调用关系通过Zipkin UI生成依赖关系图，显示了多少跟踪请求通过每个服务。
该系统让开发者可通过一个Web前端轻松的收集和分析数据，例如用户每次请求服务的处理时间等，可方便的检测系统中存在的瓶颈。

Zipkin提供了可插拔数据存储方式：In-Memory、MySql、Cassandra以及Elasticsearch。生产推荐Elasticsearch。
