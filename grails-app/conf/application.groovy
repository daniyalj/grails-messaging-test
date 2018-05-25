// Data binding ********************************************************************************************************
grails.databinding.dateFormats = ['yyyy-MM-dd HH:mm:ss', 'yyyy-MM-dd HH:mm', 'yyyy-MM-dd']



grails {
    gorm {
        //multiTenancy {
            //mode = "SCHEMA"
            //tenantResolverClass = se.demo.TenantResolver // Configured as Spring Bean in spring/resources.groovy
        //}
        
//        // default to fail on error for all queries
        failOnerror = true
//        
//        // default to auto-flush for all queries
        autoFlush = true
    }
}



grails.gorm.default.mapping = {
    id generator: 'org.hibernate.id.enhanced.SequenceStyleGenerator',
        params: [prefer_sequence_per_entity: true]
    
//    version = true
//    autoTimestamp = true
//    
//    autowire = false
}



hibernate = [
    'cache'         : [
        'queries'               : false,
        'use_second_level_cache': false,
        'use_query_cache'       : false,
        'region.factory_class'  : 'org.hibernate.cache.ehcache.SingletonEhCacheRegionFactory'
    ],
    //'dialect'       : 'net.kaleidos.hibernate.PostgresqlExtensionsDialect',
    
    //'default_schema': "${System.getenv('POSTGRES_SCHEMA')?.trim() ?: 'public'}",
    //'schemaHandler': 'com.microservice.SchemaHandler',
    
    // force object / attribute to lower case underscore separated table / column
    //'naming_strategy': 'com.microservice.NamingStrategy',
]



dataSource {
    pooled = true
    jmxExport = true
    driverClassName = 'org.h2.Driver'
    postgresql = [
        extensions = [
            sequence_per_table = true
        ]
    ]
    dbCreate = 'update'
    url = "jdbc:h2:mem:devDb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE"
    
    //schemaHandler = "com.microservice.SchemaHandler"
    
    properties {
        jmxEnabled = true
        initialSize = 1
        maxActive = 10
        minIdle = 0
        maxIdle = 5
        maxWait = 10000
        maxAge = 600000
        timeBetweenEvictionRunsMillis = 5000
        minEvictableIdleTimeMillis = 60000
        validationQuery = 'SELECT 1'
        validationQueryTimeout = 3
        validationInterval = 15000
        testOnBorrow = true
        testWhileIdle = true
        testOnReturn = false
        jdbcInterceptors = 'ConnectionState;StatementCache(max=200)'
        defaultTransactionIsolation = java.sql.Connection.TRANSACTION_READ_COMMITTED
    }
}



server = [
    'port': "${System.getenv('ADMIN_PORT')?.trim() ?: '9080'}"?.toInteger(),
    'ssl' : [
        'enabled': false
    ]
]



environments = [
    'development': [
    ],
    'test'       : [
    ],
    'production' : [
    ]
]


grails.plugin.console.csrfProtection.enabled = false












