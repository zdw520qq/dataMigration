/*
 Navicat Premium Data Transfer

 Source Server         : erp日志
 Source Server Type    : MySQL
 Source Server Version : 50171
 Source Host           : 172.21.5.1:3306
 Source Schema         : dfb_daq_yg

 Target Server Type    : MySQL
 Target Server Version : 50171
 File Encoding         : 65001

 Date: 01/07/2019 15:49:26
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for daq_yg_log
-- ----------------------------
DROP TABLE IF EXISTS `daq_yg_log`;
CREATE TABLE `daq_yg_log`  (
  `ip` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'pg ip:端口',
  `database` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'pg数据库',
  `tablename` varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT 'pg表名',
  `status` int(255) NOT NULL COMMENT '\"0\"表示成功, \"1\"表示失败, \"-1\"表示表为空',
  `data_date` date NOT NULL COMMENT 'hive分区',
  `record_time` datetime NOT NULL COMMENT '计入时间',
  `comments` text CHARACTER SET utf8 COLLATE utf8_general_ci NULL COMMENT '异常信息'
) ENGINE = MyISAM CHARACTER SET = utf8 COLLATE = utf8_general_ci ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
