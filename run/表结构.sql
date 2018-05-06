CREATE TABLE `data_collection` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `column_a` decimal(20,10) DEFAULT '0.0000000000',
  `column_b` decimal(20,10) DEFAULT '0.0000000000',
  `column_c` decimal(20,10) DEFAULT '0.0000000000',
  `column_d` decimal(20,10) DEFAULT '0.0000000000',
  `column_e` decimal(20,10) DEFAULT '0.0000000000',
  `column_f` decimal(20,10) DEFAULT '0.0000000000',
  `column_g` decimal(20,10) DEFAULT '0.0000000000',
  `column_h` decimal(20,10) DEFAULT '0.0000000000',
  `column_i` decimal(20,10) DEFAULT '0.0000000000',
  `column_j` decimal(20,10) DEFAULT '0.0000000000',
  `column_k` decimal(20,10) DEFAULT '0.0000000000',
  `column_l` decimal(20,10) DEFAULT '0.0000000000',
  `quality` varchar(20) DEFAULT '',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `update_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='数据集合';