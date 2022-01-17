

 <center><h1><b><font color='gold'>Elasticsearch-操作详解</font></b></h1></center>

# 一.索引操作

## 1.索引基础操作

```bash
#添加索引
PUT wang

#删除索引
DELETE wang

#查询以个索引信息
GET wang

#查询多个索引信息
GET wang1,wang2

#查询所有索引信息
GET _all  
```

## 2.索引的关闭与打开

```bash
#关闭索引
POST wang/_close

#打开索引
POST wang/_open
```

# 二.映射操作

## 1.映射基础操作

```bash
#为指定索引添加映射
PUT wang/_mapping
{
  "properties":{
    "name":{
      "type":"text",
      "analyzer": "ik_smart"  
    },
    "age":{
      "type":"integer"
    }
  }
}
#为指定索引映射添加字段
PUT itcast/_mapping
{
  "properties":{
    "address":{
      "type":"text",
      "analyzer": "ik_smart"  
    }
  }
}
#创建索引的同时指定映射
PUT goods
{
	"mappings": {
		"properties": {
			"title": {
				"type": "text",
				"analyzer": "ik_smart"
			},
			"price": { 
				"type": "double"
			},
			"createTime": {
				"type": "date"
			},
			"categoryName": {	
				"type": "keyword"
			}
		}
	}
}
```

# 三.文档操作

## 1.基础操作

### * Kibana控制台

>添加(指定id)

```bash
#添加/修改(指定id)
POST /itcast/_doc/5
{
  "name":"张三",
  "age":18,
  "address":"北京"
}
 
#添加文档(不指定id,生成随机id)
POST /itcast/_doc
{
  "name":"李四",
  "age":18,
  "address":"南京"
}
```

>删除(指定id)

```bash
#删除文档(指定id)
DELETE /itcast/_doc/2
```

>查询(指定id)

```bash
#查询文档(指定id)
GET /itcast/_doc/5
```

>修改文档(指定id)


```bash
#修改文档(指定id)
PUT itcast/_doc/5
{
  "name": "万手哥",
  "age": 18,
  "address": "西湖"
}

```

### * Java代码

>添加/修改

```java
     /**
     * 添加文档：如果id存在则修改，id不存在则添加
     	注意:修改时,数据会直接覆盖
     */
	@Test
    void addDoc() throws IOException {
        /*   
        HashMap<String, Object> map = new HashMap<>();
        map.put("name", "jack");
        map.put("age", "36");
        map.put("address", "西安");
        IndexRequest request = new IndexRequest("itcast").id("1").source(map);
        */
        
        //构建数据
        Goods goods = new Goods();
        goods.setId(10000.0);
        goods.setBrandName("apple");
        goods.setCreateTime(new Date());
        //构建操作请求
        IndexRequest request = 
                new IndexRequest("goods").id("1000").source(JSON.toJSONString(goods), XContentType.JSON);
        //发送操作请求
        IndexResponse resp = client.index(request, RequestOptions.DEFAULT);
        System.out.println(resp.getId());
    }
```

>删除


```java
    @Test
    void delDoc() throws IOException {
        //构建删除请求
        DeleteRequest request = new DeleteRequest("goods", "1000");
        //发送删除请求
        DeleteResponse resp = client.delete(request, RequestOptions.DEFAULT);
        System.out.println(resp.getId());

    }


```

>查询

```java
    @Test
    void getDoc() throws IOException {
        //构建查询请求
        GetRequest request = new GetRequest("goods", "536563.0");
        //发送查询请求
        GetResponse resp = client.get(request, RequestOptions.DEFAULT);
        System.out.println(resp.getSourceAsString());

    }
```

## 2.查询所有

### * Kibana控制台

```bash
# 简写
GET /goods/_search

# 查询当前索引下所有文档,默认分页,每页10条
GET /goods/_search
{
  "query": {
    "match_all": {}
    },
    "from": 0,
    "size": 20
}
```

### * Java代码

```java
 //查询所有文档
    @Test
    void matchAll() throws IOException {
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //query
        MatchAllQueryBuilder queryBuilder = QueryBuilders.matchAllQuery();
        sourceBuilder.query(queryBuilder);
        //分页
        sourceBuilder.from(0);
        sourceBuilder.size(20);
        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }

    }
```

## 3.条件查询(单字段,单查询)

### * Kibana控制台

>term- 不会对查询条件进行分词操作,直接进行等值匹配

```bash
#适合keyword 、numeric、date
GET goods/_search
{
  "query": {
    "term": {
      "categoryName": {
        "value": "手机"
      }
    }
  }
}
```

>match- 先对查询条件进行分词,再等值匹配,最后取交集或并集

```bash
#match 默认取or并集 ,可以通过operator 修改为and 取交集
GET /goods/_search
{
  "query": {
    "match": {
      "title":{
        "query":"华为手机",
        "operator": "or"
      }
    }
  }, 
  "from": 0,
  "size": 20
}
```

### * Java代码

>term

```java
 @Test
    void testTermQuery() throws IOException{
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //query
        TermQueryBuilder queryBuilder = QueryBuilders.termQuery("title", "华为");
        sourceBuilder.query(queryBuilder);

        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }

```

>match

```java
 @Test
    void testMatchQuery() throws IOException{
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //query
        MatchQueryBuilder queryBuilder = QueryBuilders.matchQuery("title", "华为手机");
        sourceBuilder.query(queryBuilder);

        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }

```

## 4.多条件查询(多字段,单查询)

### * Kibana控制台

```bash
# queryString 多条件查询
#会对查询条件进行分词。然后将分词后的查询条件和词条进行等值匹配
#默认取并集（OR) 可以识别识别query中的连接符（or 、and）

GET goods/_search
{
  "query": {
    "query_string": {
      "fields": ["title","categoryName","brandName"], 
      "query": "华为 AND 手机"
    }
  }
}
```



### * Java代码

```java
  @Test
    void testQueryString() throws IOException {
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //query
        //注意:
        //queryStringQuery()中的是查询词条,默认对其分词,可以识别AND OR 连接符
        //defaultOperator() 中 默认为OR 即多个字段匹配后的数据之间取交集 AND则是去并集
        QueryStringQueryBuilder queryBuilder = QueryBuilders.queryStringQuery("华为 AND 手机").
                field("title").field("categoryName").field("brandName").defaultOperator(Operator.OR);
        sourceBuilder.query(queryBuilder);

        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }
```

## 5.布尔查询(单/多字段,多查询)

### * Kibana控制台

```bash
#boolQuery：对多个查询条件连接。连接方式：
#must（and）：条件必须成立
#must_not（not）：条件必须不成立
#should（or）：条件可以成立
#filter：条件必须成立，性能比must高。不会计算得分
#一般先用must ,再用filter
GET goods/_search
{
  "query": {
    "bool": {
      "must": [
        {
          "term": {
            "brandName": {
              "value": "华为"
            }
          }
        }
      ],
      "filter":[ 
        {
        "term": {
          "title": "手机"
        }
       },
       {
         "range":{
          "price": {
            "gte": 2000,
            "lte": 3000
         }
         }
       }
      
      ]
    }
  }
}

```



### * Java代码

```java
@Test
    void testBoolQuery() throws IOException {
        /* ****************构建请求对象***********************/

        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //1.构建boolQuery
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        //2.构建各个查询条件
        //2.1 条件一
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brandName", "华为");
        boolQuery.must(termQueryBuilder);
        //2.2. 条件二
        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("title", "手机");
        boolQuery.filter(matchQuery);

        sourceBuilder.query(boolQuery);

        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }

```

## 6.模糊查询

### * Kibana控制台

>wildcard 通配符

```bash
#"*华*"  包含华字的
#"华*"   华字后边多个字符
#"华?"  华字后边任意字符
#"*华"或"?华" 会引发全表（全索引）扫描 注意效率问题

GET goods/_search
{
  "query": {
    "wildcard": {
      "title": {
        "value": "华*"
      }
    }
  }
}
```

>regexp 正则

```bash
# \W：匹配包括下划线的任何单词字符，等价于 [A-Z a-z 0-9_]   开头的反斜杠是转义符
# +号多次出现  (.)*为任意字符
# 正则查询取决于正则表达式的效率

GET goods/_search
{
  "query": {
    "regexp": {
      "title": "\\w+(.)*"
    }
  }
}

```

>prefix 前缀

```bash
# 前缀查询 对keyword类型支持比较好
GET goods/_search
{
  "query": {
    "prefix": {
      "brandName": {
        "value": "三"
      }
    }
  }
}
```



### * Java代码

>wildcard 通配符

```java
   @Test
    void testWildcardQuery() throws IOException{
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //query
        WildcardQueryBuilder queryBuilder = QueryBuilders.wildcardQuery("title", "华*");
        sourceBuilder.query(queryBuilder);

        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }
```

>regexp 正则

```java
   @Test
    void testRegexpQuery() throws IOException{
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //query
        RegexpQueryBuilder queryBuilder = QueryBuilders.regexpQuery("title", "\\w+(.)*");
        sourceBuilder.query(queryBuilder);

        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }

```

>prefix 前缀

```java
    @Test
    void testPrefixQuery() throws IOException{
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
        //query
        PrefixQueryBuilder queryBuilder = QueryBuilders.prefixQuery("brandName", "三");
        sourceBuilder.query(queryBuilder);

        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }
```

## 7.范围&排序查询

###  * Kibana控制台

```bash
# range 范围查询  gte 大于等于 lte 小于等于
# sort 排序查询
GET goods/_search
{
  "query": {
    "range": {
      "price": {
        "gte": 2000,
        "lte": 3000
      }
    }
  },
  "sort": [
    {
      "price": {
        "order": "desc"
      }
    }
  ]
}
```



###  * Java代码

```java
 @Test
    void testSortQuery() throws IOException {
        /* ****************构建请求对象***********************/
        //构建查询条件构建器
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();


        //范围查询
        RangeQueryBuilder rangeQuery = QueryBuilders.rangeQuery("price");
        rangeQuery.gte(2000);
        rangeQuery.lte(3000);
        sourceBuilder.query(rangeQuery);
        //排序查询
        sourceBuilder.sort("price", SortOrder.DESC);
        //构建请求对象 ,相当于 GET goods/_search
        SearchRequest searchRequest = new SearchRequest("goods");
        searchRequest.source(sourceBuilder);

        /* ****************发送操作请求***********************/

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /* ****************解析返回值Json数据***********************/

        SearchHits hits = searchResponse.getHits();
        //获取Hits数据总数
        long value = hits.getTotalHits().value;
        System.out.println(value);

        //获取Hits数据,转化为List集合
        ArrayList<Goods> goodsList = new ArrayList<>();
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            String sourceAsString = hit.getSourceAsString();
            Goods good = JSON.parseObject(sourceAsString, Goods.class);
            goodsList.add(good);
        }
        for (Goods goods : goodsList) {
            System.out.println(goods);
        }
    }
```

## 8.聚合查询

### * Kibana控制台

>指标聚合

```bash
# 相当于Mysql的聚合函数 max,min,avg,sum等
# 返回值中会单独提供一个aggs参数,作为聚合参数
GET goods/_search
{
  "query": {
    "match": {
      "title": "手机"
    }
  },
  "aggs": {
    "max_price": {
      "max": {
        "field": "price"
      }
    }
  }
}
```

>桶聚合

```bash
# 相当于Mysql的Group By功能 
# 注意:text类型字段无法进行分组
GET goods/_search
{
  "query": {
    "match": {
      "title": "手机"
    }
  },
  "aggs": {
    "goods_brands": {
      "terms": {
        "field": "brandName",
        "size": 100
      }
    }
  }
}
```



###  * Java代码

>指标聚合

```java

```

>桶聚合

## 9.高亮查询

### * Kibana控制台

### * Java代码