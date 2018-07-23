/*
Source Server         : 127.0.0.1
Source Host           : 127.0.0.1:3306
Source Database       : simple_pay

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2018-07-23 13:23:30
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for pay_payment_journal
-- ----------------------------
DROP TABLE IF EXISTS `pay_payment_journal`;
CREATE TABLE `pay_payment_journal` (
  `payment_journal_id` bigint(20) NOT NULL COMMENT '主键id',
  `payment_deal_no` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '支付流水号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `pay_app_id` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '支付appid',
  `merchant_id` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '支付商户号',
  `merchant_order_no` varchar(200) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户订单号',
  `payment_deal_id` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '第三方交易流水号',
  `pay_status` int(11) NOT NULL DEFAULT 0 COMMENT '0-创建（待支付）\r\n            1-唤起收银台\r\n            2-支付\r\n            3-退款',
  `pay_code` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '支付编码',
  `pay_mode` int(11) NOT NULL DEFAULT 0 COMMENT '支付方式',
  `pay_amount` int(11) NOT NULL DEFAULT 0 COMMENT '自费金额 最小单位:分',
  `account_amount` int(11) NOT NULL DEFAULT 0 COMMENT '个帐金额 最小单位：分',
  `medicare_amount` int(11) NOT NULL DEFAULT 0 COMMENT '统筹金额 最小单位：分',
  `insurance_amount` int(11) NOT NULL DEFAULT 0 COMMENT '记账合计金额 最小单位：分',
  `total_amount` int(11) NOT NULL DEFAULT 0 COMMENT '总金额 最小单位：分',
  `description` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `extra_params` varchar(2048) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '扩展字段 JSON格式串',
  `create_time` datetime DEFAULT NULL,
  `pay_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `data_source` varchar(20) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '数据来源',
  PRIMARY KEY (`payment_journal_id`),
  UNIQUE KEY `idx_payment_deal_no` (`payment_deal_no`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_pay_mode` (`pay_mode`),
  KEY `idx_pay_app_id` (`pay_app_id`) USING BTREE,
  KEY `idx_create_time` (`create_time`) USING BTREE,
  KEY `idx_pay_time` (`pay_time`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='支付流水表';

-- ----------------------------
-- Table structure for pay_payment_setting
-- ----------------------------
DROP TABLE IF EXISTS `pay_payment_setting`;
CREATE TABLE `pay_payment_setting` (
  `payment_setting_id` bigint(20) NOT NULL COMMENT '主键id',
  `pay_app_id` varchar(128) COLLATE utf8_unicode_ci NOT NULL COMMENT '支付appid',
  `merchant_id` varchar(64) COLLATE utf8_unicode_ci NOT NULL COMMENT '商户号',
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `user_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `app_secret` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '授权密钥',
  `api_secret` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '接口密钥',
  `certificate_path` varchar(256) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '授权证书',
  `certificate_pwd` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `medicare_api_secret` varchar(1024) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '医疗支付接口密钥',
  `public_key` varchar(4098) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '公钥',
  `private_key` varchar(4098) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '私钥',
  `pay_public_key` varchar(4098) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '第三方支付公钥',
  `partner_id` varchar(128) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '合作者id',
  `pay_code` varchar(256) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '支付编码',
  `pay_mode` int(11) DEFAULT NULL COMMENT '支付方式\r\n            ',
  `notify_url` varchar(1024) COLLATE utf8_unicode_ci DEFAULT '',
  `notify_key` varchar(128) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '异步通知到其它客户端的key',
  `enable_flag` smallint(6) NOT NULL DEFAULT 0 COMMENT '是否启用 0-不可用  1-启用',
  `description` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '描述',
  `personal_transfer_str` varchar(512) COLLATE utf8_unicode_ci DEFAULT NULL,
  `parent_payment_setting_id` int(11) DEFAULT NULL COMMENT '父id',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`payment_setting_id`),
  UNIQUE KEY `AK_idx_app_id_merchant_id` (`pay_app_id`,`merchant_id`) USING BTREE,
  KEY `idx_pay_app_id` (`pay_app_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='支付配置表';

-- ----------------------------
-- Table structure for sys_user
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL COMMENT '主键id',
  `login_name` varchar(20) COLLATE utf8_unicode_ci NOT NULL COMMENT '登录用户',
  `password` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '密码',
  `salt` varchar(32) COLLATE utf8_unicode_ci NOT NULL COMMENT '盐',
  `user_name` varchar(32) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '用户姓名',
  `email` varchar(64) COLLATE utf8_unicode_ci DEFAULT NULL COMMENT '邮件地址',
  `mobile` varchar(32) COLLATE utf8_unicode_ci DEFAULT '' COMMENT '手机',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  `update_time` datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `AK_idx_login_name` (`login_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci COMMENT='用户表';
