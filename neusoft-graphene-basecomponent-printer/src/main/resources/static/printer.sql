
/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8mb4 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;

-- 导出  表 vie_sys.t_printer_client 结构
DROP TABLE IF EXISTS `t_printer_client`;
CREATE TABLE IF NOT EXISTS `t_printer_client` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `key` varchar(64) DEFAULT NULL,
  `name` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `key` (`key`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;

-- 正在导出表  vie_sys.t_printer_client 的数据：~3 rows (大约)
DELETE FROM `t_printer_client`;
/*!40000 ALTER TABLE `t_printer_client` DISABLE KEYS */;
INSERT INTO `t_printer_client` (`id`, `key`, `name`) VALUES
	(1, '736b37e5118b51e2b8f6de00392ad1a7', '众安保险'),
	(2, 'bc63f3d7f4ab58818283e9ad697cd05f', '豹云科技'),
	(3, 'dd2dada7e27f51a5832dc4e7903be28d', '本地测试');
/*!40000 ALTER TABLE `t_printer_client` ENABLE KEYS */;


-- 导出  表 vie_sys.t_printer_log 结构
DROP TABLE IF EXISTS `t_printer_log`;
CREATE TABLE IF NOT EXISTS `t_printer_log` (
  `client_key` varchar(32) DEFAULT NULL,
  `template_id` bigint(20) NOT NULL,
  `file_type` varchar(16) DEFAULT NULL,
  `out_type` tinyint(4) DEFAULT NULL COMMENT '1 file 2 stream',
  `build_time` int(11) DEFAULT NULL COMMENT '排版耗时',
  `export_time` int(11) DEFAULT NULL COMMENT '输出耗时',
  `page` int(11) DEFAULT NULL COMMENT '页数',
  `result` tinyint(4) DEFAULT NULL COMMENT '1成功 2失败',
  `ip` varchar(32) DEFAULT NULL,
  `operate_time` datetime DEFAULT NULL COMMENT '操作时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- 导出  表 vie_sys.t_printer_sign 结构
DROP TABLE IF EXISTS `t_printer_sign`;
CREATE TABLE IF NOT EXISTS `t_printer_sign` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `keystore` varchar(64) DEFAULT NULL,
  `password` varchar(32) DEFAULT NULL,
  `scope` varchar(32) DEFAULT NULL,
  `reason` varchar(128) DEFAULT NULL,
  `location` varchar(128) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=DYNAMIC;

-- 正在导出表  vie_sys.t_printer_sign 的数据：~1 rows (大约)
DELETE FROM `t_printer_sign`;
/*!40000 ALTER TABLE `t_printer_sign` DISABLE KEYS */;
INSERT INTO `t_printer_sign` (`id`, `keystore`, `password`, `scope`, `reason`, `location`) VALUES
	(1, 'lex_test.p12', 'Abcd1234', 'test', NULL, NULL);
/*!40000 ALTER TABLE `t_printer_sign` ENABLE KEYS */;


-- 导出  表 vie_sys.t_printer_template 结构
DROP TABLE IF EXISTS `t_printer_template`;
CREATE TABLE IF NOT EXISTS `t_printer_template` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `code` varchar(32) DEFAULT NULL,
  `name` varchar(64) DEFAULT NULL,
  `template_file` varchar(64) DEFAULT NULL,
  `work_dir` varchar(64) DEFAULT NULL,
  `test_file` varchar(64) DEFAULT NULL,
  `sign` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

-- 正在导出表  vie_sys.t_printer_template 的数据：~8 rows (大约)
DELETE FROM `t_printer_template`;
/*!40000 ALTER TABLE `t_printer_template` DISABLE KEYS */;
INSERT INTO `t_printer_template` (`id`, `code`, `name`, `template_file`, `work_dir`, `test_file`, `sign`) VALUES
	(1, 'iyb_proposal', 'iyb建议书1', 'template.xml', 'iyb_proposal', 'test.json', NULL),
	(2, 'neusoft1', '众安普通保单', 'template.xml', 'neusoft1', 'test.json', '1'),
	(3, 'neusoft2', '众安发票', 'template.xml', 'neusoft2', 'test.json', NULL),
	(4, 'iyb_quote', 'iyb报价', 'template.xml', 'iyb_quote', 'test.json', NULL),
	(5, 'neusoft3', '众安雇主责任险', 'template.xml', 'neusoft3', 'test.json', '1'),
	(6, 'test1', '测试1', 'template.xml', '6', 'test.json', NULL),
	(7, 'neusoft4', '尊享e生全保通', 'template.xml', 'neusoft4', 'test.json', '1'),
	(8, 'neusoft5', '众安批单', 'template.xml', 'neusoft5', 'test.json', NULL);
/*!40000 ALTER TABLE `t_printer_template` ENABLE KEYS */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IF(@OLD_FOREIGN_KEY_CHECKS IS NULL, 1, @OLD_FOREIGN_KEY_CHECKS) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;



select * from printer.t_printer_log order by operate_time desc;

select * from printer.t_printer_sign;

select * from printer.t_printer_template;

delete from printer.t_printer_template where id =14;
commit;

INSERT INTO `t_printer_template` (`id`, `code`, `name`, `template_file`, `work_dir`, `test_file`, `sign`)
VALUES
(9, 'neusoft_health', 'G_健康险', 'template.xml', 'neusoft_health', 'test.json', NULL);

update `t_printer_template` set sign = 1 where id = 9;

INSERT INTO `t_printer_template` (`id`, `code`, `name`, `template_file`, `work_dir`, `test_file`, `sign`)
VALUES
(10, 'neusoft_familyfinace', 'G_家财险', 'template.xml', 'neusoft_familyfinace', 'test.json', NULL);

explain select * from printer.t_printer_client where t_printer_client.key = '736b37e5118b51e2b8f6de00392ad1a7';
explain select * from printer.t_printer_client where id = '1';
explain select * from printer.t_printer_client where name = '众安保险';

select * from printer.t_printer_template;

INSERT INTO `t_printer_template` (`id`, `code`, `name`, `template_file`, `work_dir`, `test_file`, `sign`)
VALUES
(11, 'g_corporate_insurance', 'g_corporate_insurance', 'template.xml', 'g_corporate_insurance', 'test.json', NULL),
(12, 'g_credit_insurance', 'g_credit_insurance', 'template.xml', 'g_credit_insurance', 'test.json', NULL),
(13, 'g_guarantee_insurance', 'g_guarantee_insurance', 'template.xml', 'g_guarantee_insurance', 'test.json', NULL),
(14, 'g_property_insurance', 'g_property_insurance', 'template.xml', 'g_property_insurance', 'test.json', NULL);

INSERT INTO `t_printer_template` (`id`, `code`, `name`, `template_file`, `work_dir`, `test_file`, `sign`)
VALUES
(14, 'endorsement', 'endorsement', 'template.xml', 'endorsement', 'test.json', NULL);

delete from printer.t_printer_template where id =11;
commit;



INSERT INTO `t_printer_template` (`id`, `code`, `name`, `template_file`, `work_dir`, `test_file`, `sign`)
VALUES
(9, 'zhong_growup2', '成长无忧', 'template.xml', 'zhong_growup2', 'test.json', NULL);