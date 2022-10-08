-- pay_gateway.amount_flow definition

CREATE TABLE `amount_flow` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `merchant_id` varchar(32) DEFAULT NULL COMMENT '商户ID',
  `trade_id` varchar(100) DEFAULT NULL COMMENT '交易ID',
  `channel_trade_id` varchar(100) DEFAULT NULL COMMENT '支付渠道返回的流水ID',
  `batch_id` varchar(100) DEFAULT NULL COMMENT '批次ID(如：部分退款)',
  `amount` decimal(15,5) DEFAULT NULL COMMENT '金额',
  `flow_type` bit(1) DEFAULT NULL COMMENT '流水类型：0，进；1:出',
  `amount_unit_type` smallint(6) DEFAULT '0' COMMENT '币种类型，0:rmb,1:其他',
  `rate` double DEFAULT '1' COMMENT '外汇比率',
  `created_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ix_amount_flow` (`merchant_id`,`trade_id`,`channel_trade_id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


-- pay_gateway.channel_cert definition

CREATE TABLE `channel_cert` (
  `id` int(11) NOT NULL,
  `merchant_id` varchar(100) DEFAULT NULL COMMENT '商户id',
  `channel_platform` varchar(20) DEFAULT NULL COMMENT '渠道平台WX ZFB',
  `application_cert` varchar(2000) DEFAULT NULL COMMENT '证书',
  `application_key` varchar(2000) DEFAULT NULL COMMENT '证书私钥',
  `serial_no` varchar(50) DEFAULT NULL COMMENT '证书序列号',
  `cert_platform_pub` varchar(500) DEFAULT NULL COMMENT '证书平台公钥',
  `token_api_url` varchar(255) DEFAULT NULL COMMENT '获取token APi',
  `token_api_method` varchar(10) DEFAULT 'GET' COMMENT '获取token APi method',
  `cert_platform_pri` varchar(2000) DEFAULT NULL COMMENT '证书平台私钥',
  `cert_platform_serial_no` varchar(50) DEFAULT NULL,
  `cert_expire_time` datetime(3) DEFAULT NULL COMMENT '证书过期时间',
  `created_date_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `last_modified_date_time` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- pay_gateway.channel_config definition

CREATE TABLE `channel_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_name` varchar(100) DEFAULT NULL COMMENT '渠道名称',
  `channel_code` varchar(200) DEFAULT NULL COMMENT '渠道编码，用于颁发给平台',
  `service_url` varchar(2000) DEFAULT NULL COMMENT '第三方支付服务URL',
  `merchant_id` varchar(100) DEFAULT NULL COMMENT '商户ID',
  `app_id` varchar(512) DEFAULT NULL COMMENT '第三方支付服务颁发的APPID',
  `public_key` varchar(2000) DEFAULT NULL COMMENT '公钥',
  `private_key` varchar(2000) DEFAULT NULL COMMENT '渠道颁发的私密key',
  `created_user_id` int(11) DEFAULT NULL COMMENT '创建数据用户ID',
  `conn_timeout_ms` int(11) DEFAULT '3000' COMMENT '连接超时毫秒',
  `red_timeout_ms` int(11) DEFAULT '3000' COMMENT '返回结果超时毫秒',
  `extend_parameter` text COMMENT '扩展参数JSON',
  `created_datetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT '是否逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ix_channel_code` (`channel_code`(191)) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2209 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


-- pay_gateway.channel_platform_config definition

CREATE TABLE `channel_platform_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_platform` varchar(10) DEFAULT 'WX' COMMENT '第三方平台WX ZFB',
  `platform_describe` varchar(200) DEFAULT NULL COMMENT '平台code描述',
  `channel_short_code` varchar(100) DEFAULT NULL COMMENT '支付渠道短码',
  `platform_code` varchar(100) DEFAULT NULL COMMENT '平台code',
  `channel_pay_code` varchar(100) DEFAULT NULL COMMENT '支付渠道ID',
  `channel_pay_query_code` varchar(100) DEFAULT NULL COMMENT '支付平台查询Code',
  `channel_refund_code` varchar(100) DEFAULT NULL,
  `channel_refund_query_code` varchar(100) DEFAULT NULL,
  `channel_cancel_code` varchar(100) DEFAULT NULL COMMENT '支付平台取消',
  `channel_add_receiver` varchar(100) DEFAULT NULL,
  `business_describe` varchar(100) DEFAULT NULL COMMENT '业务描述',
  `created_datetime` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT '是否逻辑删除',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `ix_channel_code` (`channel_short_code`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=1401 DEFAULT CHARSET=utf8mb4;


-- pay_gateway.notify_record definition

CREATE TABLE `notify_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `notify_type` varchar(100) DEFAULT NULL COMMENT '通知类型',
  `notify_id` varchar(100) DEFAULT NULL COMMENT '回调ID',
  `channel_code` varchar(100) DEFAULT NULL COMMENT '支付渠道回调来源',
  `created_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '回调时间',
  `body` text COMMENT '回调报文',
  `handler_id` varchar(32) DEFAULT NULL COMMENT '回调处理逻辑ID',
  `head` text COMMENT 'heads数据',
  `decrypt_body` text COMMENT '解密数据',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


-- pay_gateway.pay_call_record definition

CREATE TABLE `pay_call_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform_code` varchar(100) DEFAULT NULL COMMENT '平台CODE',
  `business_code` varchar(100) DEFAULT NULL,
  `channel_code` varchar(100) DEFAULT NULL,
  `trade_id` varchar(100) DEFAULT NULL COMMENT '交易ID',
  `batch_id` varchar(100) DEFAULT NULL COMMENT '批次ID',
  `request_body` longtext COMMENT '请求报文（JSON）',
  `response_code` varchar(100) DEFAULT NULL COMMENT '响应报文CODE，渠道返回的CODE',
  `response_message` varchar(255) DEFAULT NULL COMMENT '响应消息',
  `request_id` varchar(32) DEFAULT NULL COMMENT '请求上下文Id',
  `system_code` int(11) DEFAULT '0' COMMENT '系统code,0:调用成功，非0:调用失败',
  `system_message` text COMMENT '系统消息，一般用于记录调用失败异常堆栈',
  `call_cost_time` int(11) DEFAULT NULL COMMENT '请求耗时',
  `origin_resp_body` text COMMENT '支付服务返回的原始报文',
  `created_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


-- pay_gateway.payment_order definition

CREATE TABLE `payment_order` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform_code` varchar(100) DEFAULT NULL COMMENT '平台，如：愚公,番丽,XX',
  `platform_sub_type` varchar(100) DEFAULT NULL COMMENT '平台二级业务,如：愚公门店，愚公商城',
  `order_type` int(11) DEFAULT '0' COMMENT '0:支付订单；1:退款订单',
  `trade_id` varchar(100) DEFAULT NULL COMMENT '品特交易订单ID',
  `payment_user_id` varchar(255) DEFAULT NULL COMMENT '支付或退款用户ID',
  `channel_trade_id` varchar(100) DEFAULT NULL COMMENT '支付渠道返回支付订单ID',
  `channel_code` varchar(100) DEFAULT NULL COMMENT '支付渠道CODE',
  `status_code` int(11) DEFAULT NULL COMMENT '付款状态：0待支付 1,支付成功 2支付超时 3支付失败 4支付关闭 5退款中 6退款成功 7退款失败 8支付撤销 50000未知状态',
  `refund_batch_id` varchar(100) DEFAULT NULL COMMENT '退款批次ID（用于退款）',
  `channel_advance_payment_id` varchar(100) DEFAULT NULL COMMENT '支付渠道预支付订单ID',
  `channel_refund_id` varchar(100) DEFAULT NULL COMMENT '支付渠道退款ID',
  `payment_timeout` datetime(3) DEFAULT NULL COMMENT '支付超时，超时取消支付',
  `pay_type` smallint(6) DEFAULT NULL COMMENT '支付方式：1 微信 2 支付宝 3 愚公币',
  `amount` decimal(15,5) DEFAULT NULL COMMENT '支付金额',
  `payment_date_time` datetime DEFAULT NULL COMMENT '支付、退款时间',
  `created_date_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '数据创建时间',
  `channel_trade_status` varchar(100) DEFAULT NULL COMMENT '渠道支付状态',
  `channel_refund_status` varchar(100) DEFAULT NULL COMMENT '渠道退款状态',
  `merchant_id` varchar(100) DEFAULT NULL COMMENT '商户ID（如：门店ID，社区团团长，分销商ID）',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT '是否逻辑删除',
  `order_tag` int(11) DEFAULT NULL COMMENT '支付订单业务标签：0:下单，1:定金，2:尾款，3:退款，4:充值',
  `request_id` varchar(36) DEFAULT NULL COMMENT '请求上下文Id',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `ix_trade_id_batch_id` (`trade_id`,`refund_batch_id`(36)) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


-- pay_gateway.profit_sharing definition

CREATE TABLE `profit_sharing` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `trade_id` varchar(100) DEFAULT NULL COMMENT '平台交易Id',
  `fz_order_Id` varchar(64) DEFAULT NULL COMMENT '分账订单ID',
  `channel_fz_order_id` varchar(100) DEFAULT NULL COMMENT '渠道分账ID',
  `fz_sub_mchid` varchar(100) DEFAULT NULL COMMENT '出账商户ID',
  `status` int(11) DEFAULT NULL COMMENT '分账状态',
  `created_datetime` datetime DEFAULT NULL COMMENT '分账发起时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;


-- pay_gateway.profit_sharing_receivers definition

CREATE TABLE `profit_sharing_receivers` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `channel_platform` varchar(10) DEFAULT NULL COMMENT '渠道',
  `service_mch_id` varchar(50) DEFAULT NULL COMMENT '服务商',
  `sub_mch_id` varchar(50) DEFAULT NULL COMMENT '子商户',
  `receiver_type` varchar(50) DEFAULT NULL COMMENT '类型',
  `receiver_account` varchar(50) DEFAULT NULL COMMENT '分账接收商户',
  `created_user_id` int(11) DEFAULT NULL COMMENT '创建人',
  `created_date_time` datetime(3) DEFAULT NULL COMMENT '创建时间',
  `last_modified_user_id` int(11) DEFAULT NULL COMMENT '修改人',
  `last_modified_datetime` datetime(3) DEFAULT NULL COMMENT '修改时间',
  `is_deleted` bit(1) DEFAULT b'0' COMMENT '是否删除',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4;


-- pay_gateway.sys_log definition

CREATE TABLE `sys_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `platform_code` varchar(100) DEFAULT NULL,
  `business_code` varchar(100) DEFAULT NULL,
  `channel_code` varchar(100) DEFAULT NULL,
  `sys_code` int(11) DEFAULT NULL,
  `sys_message` text,
  `created_date_time` datetime DEFAULT CURRENT_TIMESTAMP,
  `request_id` varchar(36) DEFAULT NULL,
  `request_body` text,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;