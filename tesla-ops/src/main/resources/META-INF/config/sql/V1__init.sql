SET FOREIGN_KEY_CHECKS=0;
-- ----------------------------
-- Table structure for `sys_dept`
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept` (
  `dept_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '上级部门ID，一级部门为0',
  `name` varchar(50) DEFAULT NULL COMMENT '部门名称',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `del_flag` tinyint(4) DEFAULT '0' COMMENT '是否删除  -1：已删除  0：正常',
  PRIMARY KEY (`dept_id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8 COMMENT='部门管理';

-- ----------------------------
-- Records of sys_dept
-- ----------------------------
INSERT INTO `sys_dept` VALUES ('6', '0', '基础架构', '1', '1');
INSERT INTO `sys_dept` VALUES ('9', '0', '业务研发', '2', '1');

-- ----------------------------
-- Table structure for `sys_log`
-- ----------------------------
DROP TABLE IF EXISTS `sys_log`;
CREATE TABLE `sys_log` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户id',
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `operation` varchar(50) DEFAULT NULL COMMENT '用户操作',
  `time` int(11) DEFAULT NULL COMMENT '响应时间',
  `method` varchar(200) DEFAULT NULL COMMENT '请求方法',
  `params` varchar(5000) DEFAULT NULL COMMENT '请求参数',
  `ip` varchar(64) DEFAULT NULL COMMENT 'IP地址',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7956 DEFAULT CHARSET=utf8 COMMENT='系统日志';

-- ----------------------------
-- Records of sys_log
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu` (
  `menu_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `parent_id` bigint(20) DEFAULT NULL COMMENT '父菜单ID，一级菜单为0',
  `name` varchar(50) DEFAULT NULL COMMENT '菜单名称',
  `url` varchar(200) DEFAULT NULL COMMENT '菜单URL',
  `perms` varchar(500) DEFAULT NULL COMMENT '授权(多个用逗号分隔，如：user:list,user:create)',
  `type` int(11) DEFAULT NULL COMMENT '类型   0：目录   1：菜单   2：按钮',
  `icon` varchar(50) DEFAULT NULL COMMENT '菜单图标',
  `order_num` int(11) DEFAULT NULL COMMENT '排序',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`menu_id`)
) ENGINE=InnoDB AUTO_INCREMENT=102 DEFAULT CHARSET=utf8 COMMENT='菜单管理';

-- ----------------------------
-- Records of sys_menu
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('1', '0', '系统管理', null, null, '0', 'fa fa-desktop', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('2', '0', '系统监控', null, null, '0', 'fa fa-video-camera', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('3', '0', 'API消费端管理', null, null, '0', 'fa fa-eye', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('4', '0', 'API管理', null, null, '0', 'fa fa-tachometer', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('5', '0', '组件规则管理', null, null, '0', 'fa fa-tachometer', '4', '2017-08-09 23:06:55', '2017-08-14 14:13:43');


-- ----------------------------
-- 系统管理开始
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('11', '1', '系统菜单', 'sys/menu', 'sys:menu:menu', '1', 'fa fa-th-list', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('12', '1', '用户管理', 'sys/user', 'sys:user:user', '1', 'fa fa-user', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('13', '1', '角色管理', 'sys/role', 'sys:role:role', '1', 'fa fa-paw', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('14', '1', '部门管理', 'sys/dept', 'sys:dept:dept', '1', 'fa fa-users', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');



INSERT INTO `sys_menu` VALUES ('21', '11', '新增', '', 'sys:menu:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('22', '11', '批量删除', '', 'sys:menu:batchRemove', '2','', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('23', '11', '编辑', '', 'sys:menu:edit', '2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('24', '11', '删除', '', 'sys:menu:remove', '2', '', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');



INSERT INTO `sys_menu` VALUES ('25', '12', '新增', '', 'sys:user:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('26', '12', '编辑', '', 'sys:user:edit', '2', '', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('27', '12', '删除', '', 'sys:user:remove','2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('28', '12', '批量删除', '', 'sys:user:batchRemove', '2', '', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('29', '12', '停用', '', 'sys:user:disable', '2', '', '4', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('30', '12', '重置密码', '', 'sys:user:resetPwd', '2', '', '5', '2017-08-09 23:06:55', '2017-08-14 14:13:43');

INSERT INTO `sys_menu` VALUES ('31', '13', '新增', '', 'sys:role:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('32', '13', '批量删除', '', 'sys:role:batchRemove', '2','', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('33', '13', '编辑', '', 'sys:role:edit', '2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('34', '13', '删除', '', 'sys:role:remove', '2', '', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');


INSERT INTO `sys_menu` VALUES ('35', '14', '增加', '', 'sys:dept:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('36', '14', '刪除', '', 'sys:dept:remove', '2', '', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('37', '14', '编辑', '', 'sys:dept:edit', '2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
-- ----------------------------
-- 系统管理结束
-- ----------------------------

-- ----------------------------
-- 系统监控开始
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('38', '2', '在线用户', 'sys/online', 'sys:monitor:online', '1', 'fa fa-user', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('39', '2', '系统日志', 'sys/log', 'sys:monitor:log', '1', 'fa fa-warning', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('40', '2', '运行监控', 'sys/log/run', 'sys:monitor:run', '1', 'fa fa-caret-square-o-right', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');

-- ----------------------------
-- 系统监控结束
-- ----------------------------

-- ----------------------------
-- API管理开始
-- ----------------------------

INSERT INTO `sys_menu` VALUES ('50', '4', 'API列表', 'gateway/api', 'gateway:api:api', '1', 'fa fa-area-chart', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('51', '4', 'API分组列表', 'gateway/apigroup', 'gateway:apigroup:apigroup', '1', 'fa fa-area-chart', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');

INSERT INTO `sys_menu` VALUES ('52', '50', '新增', '', 'gateway:api:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('53', '50', '批量删除', '', 'gateway:api:batchRemove', '2','', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('54', '50', '编辑', '', 'gateway:api:edit', '2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('55', '50', '删除', '', 'gateway:api:remove', '2', '', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');


INSERT INTO `sys_menu` VALUES ('56', '51', '新增', '', 'gateway:apigroup:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('57', '51', '批量删除', '', 'gateway:apigroup:batchRemove', '2','', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('58', '51', '编辑', '', 'gateway:apigroup:edit', '2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('59', '51', '删除', '', 'gateway:apigroup:remove', '2', '', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');

-- ----------------------------
-- API管理结束
-- ----------------------------

 



-- ----------------------------
-- Filter管理开始
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('60', '5', '通用组件规则列表', 'filter/sharerule', 'filter:rule:rule', '1', 'fa fa-warning', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('61', '5', 'API组件规则列表', 'filter/bizrule', 'filter:rule:rule', '1', 'fa fa-warning', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('62', '60', '新增', '', 'filter:rule:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('63', '60', '批量删除', '', 'filter:rule:batchRemove', '2','', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('64', '60', '编辑', '', 'filter:rule:edit', '2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('65', '60', '删除', '', 'filter:rule:remove', '2', '', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');

-- ----------------------------
-- Filter管理结束
-- ----------------------------



-- ----------------------------
-- oauth2开始
-- ----------------------------
INSERT INTO `sys_menu` VALUES ('70', '3', '客户端管理', 'sys/oauth2/client', 'sys:oauth2:listclient', '1', 'fa fa-th-list', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('71', '3', '令牌监控', 'sys/oauth2/token', 'sys:oauth2:listToken', '1', 'fa fa-user', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');

INSERT INTO `sys_menu` VALUES ('80', '70', '新增', '', 'sys:oauth2:add', '2', '', '0', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('81', '70', '批量删除', '', 'sys:oauth2:batchRemove', '2','', '1', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('82', '70', '编辑', '', 'sys:oauth2:edit', '2', '', '2', '2017-08-09 23:06:55', '2017-08-14 14:13:43');
INSERT INTO `sys_menu` VALUES ('83', '70', '删除', '', 'sys:oauth2:remove', '2', '', '3', '2017-08-09 23:06:55', '2017-08-14 14:13:43');

-- ----------------------------
-- oauth2结束
-- ----------------------------

-- ----------------------------
-- Table structure for `sys_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role` (
  `role_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_name` varchar(100) DEFAULT NULL COMMENT '角色名称',
  `role_sign` varchar(100) DEFAULT NULL COMMENT '角色标识',
  `remark` varchar(100) DEFAULT NULL COMMENT '备注',
  `user_id_create` bigint(255) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8 COMMENT='角色';

-- ----------------------------
-- Records of sys_role
-- ----------------------------
INSERT INTO `sys_role` VALUES ('1', '超级用户角色', 'admin', '拥有最高权限', '2', '2017-08-12 00:43:52', '2017-08-12 19:14:59');
INSERT INTO `sys_role` VALUES ('2', '普通用户', 'user', '普通用户',  '2', '2017-08-12 00:43:52', '2017-08-12 19:14:59');
-- ----------------------------
-- Table structure for `sys_role_menu`
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu`;
CREATE TABLE `sys_role_menu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  `menu_id` bigint(20) DEFAULT NULL COMMENT '菜单ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2974 DEFAULT CHARSET=utf8 COMMENT='角色与菜单对应关系';

-- ----------------------------
-- Records of sys_role_menu
-- ----------------------------
INSERT INTO `sys_role_menu` VALUES ('1', '1', '1');
INSERT INTO `sys_role_menu` VALUES ('2', '1', '2');
INSERT INTO `sys_role_menu` VALUES ('3', '1', '3');
INSERT INTO `sys_role_menu` VALUES ('4', '1', '4');
INSERT INTO `sys_role_menu` VALUES ('5', '1', '5');
INSERT INTO `sys_role_menu` VALUES ('6', '1', '6');
INSERT INTO `sys_role_menu` VALUES ('7', '1', '7');
INSERT INTO `sys_role_menu` VALUES ('8', '1', '8');
INSERT INTO `sys_role_menu` VALUES ('9', '1', '9');
INSERT INTO `sys_role_menu` VALUES ('10', '1', '10');
INSERT INTO `sys_role_menu` VALUES ('11', '1', '11');
INSERT INTO `sys_role_menu` VALUES ('12', '1', '12');
INSERT INTO `sys_role_menu` VALUES ('13', '1', '13');
INSERT INTO `sys_role_menu` VALUES ('14', '1', '14');
INSERT INTO `sys_role_menu` VALUES ('15', '1', '15');
INSERT INTO `sys_role_menu` VALUES ('16', '1', '16');
INSERT INTO `sys_role_menu` VALUES ('17', '1', '17');
INSERT INTO `sys_role_menu` VALUES ('18', '1', '18');
INSERT INTO `sys_role_menu` VALUES ('19', '1', '19');
INSERT INTO `sys_role_menu` VALUES ('20', '1', '20');
INSERT INTO `sys_role_menu` VALUES ('21', '1', '21');
INSERT INTO `sys_role_menu` VALUES ('22', '1', '22');
INSERT INTO `sys_role_menu` VALUES ('23', '1', '23');
INSERT INTO `sys_role_menu` VALUES ('24', '1', '24');
INSERT INTO `sys_role_menu` VALUES ('25', '1', '25');
INSERT INTO `sys_role_menu` VALUES ('26', '1', '26');
INSERT INTO `sys_role_menu` VALUES ('27', '1', '27');
INSERT INTO `sys_role_menu` VALUES ('28', '1', '28');
INSERT INTO `sys_role_menu` VALUES ('29', '1', '29');
INSERT INTO `sys_role_menu` VALUES ('30', '1', '30');
INSERT INTO `sys_role_menu` VALUES ('31', '1', '31');
INSERT INTO `sys_role_menu` VALUES ('32', '1', '32');
INSERT INTO `sys_role_menu` VALUES ('33', '1', '33');
INSERT INTO `sys_role_menu` VALUES ('34', '1', '34');
INSERT INTO `sys_role_menu` VALUES ('35', '1', '35');
INSERT INTO `sys_role_menu` VALUES ('36', '1', '36');
INSERT INTO `sys_role_menu` VALUES ('37', '1', '37');
INSERT INTO `sys_role_menu` VALUES ('38', '1', '38');
INSERT INTO `sys_role_menu` VALUES ('39', '1', '39');
INSERT INTO `sys_role_menu` VALUES ('40', '1', '40');
INSERT INTO `sys_role_menu` VALUES ('41', '1', '41');
INSERT INTO `sys_role_menu` VALUES ('42', '1', '42');
INSERT INTO `sys_role_menu` VALUES ('43', '1', '43');
INSERT INTO `sys_role_menu` VALUES ('44', '1', '44');
INSERT INTO `sys_role_menu` VALUES ('45', '1', '45');
INSERT INTO `sys_role_menu` VALUES ('46', '1', '46');
INSERT INTO `sys_role_menu` VALUES ('47', '1', '47');
INSERT INTO `sys_role_menu` VALUES ('48', '1', '48');
INSERT INTO `sys_role_menu` VALUES ('49', '1', '49');
INSERT INTO `sys_role_menu` VALUES ('50', '1', '50');
INSERT INTO `sys_role_menu` VALUES ('51', '1', '51');
INSERT INTO `sys_role_menu` VALUES ('52', '1', '52');
INSERT INTO `sys_role_menu` VALUES ('53', '1', '53');
INSERT INTO `sys_role_menu` VALUES ('54', '1', '54');
INSERT INTO `sys_role_menu` VALUES ('55', '1', '55');
INSERT INTO `sys_role_menu` VALUES ('56', '1', '56');
INSERT INTO `sys_role_menu` VALUES ('57', '1', '57');
INSERT INTO `sys_role_menu` VALUES ('58', '1', '58');
INSERT INTO `sys_role_menu` VALUES ('59', '1', '59');
INSERT INTO `sys_role_menu` VALUES ('60', '1', '60');
INSERT INTO `sys_role_menu` VALUES ('61', '1', '61');
INSERT INTO `sys_role_menu` VALUES ('62', '1', '62');
INSERT INTO `sys_role_menu` VALUES ('63', '1', '63');
INSERT INTO `sys_role_menu` VALUES ('64', '1', '64');
INSERT INTO `sys_role_menu` VALUES ('65', '1', '65');
INSERT INTO `sys_role_menu` VALUES ('66', '1', '66');
INSERT INTO `sys_role_menu` VALUES ('67', '1', '67');
INSERT INTO `sys_role_menu` VALUES ('68', '1', '68');
INSERT INTO `sys_role_menu` VALUES ('69', '1', '69');
INSERT INTO `sys_role_menu` VALUES ('70', '1', '70');
INSERT INTO `sys_role_menu` VALUES ('71', '1', '71');
INSERT INTO `sys_role_menu` VALUES ('72', '1', '72');
INSERT INTO `sys_role_menu` VALUES ('73', '1', '73');
INSERT INTO `sys_role_menu` VALUES ('74', '1', '74');
INSERT INTO `sys_role_menu` VALUES ('75', '1', '75');
INSERT INTO `sys_role_menu` VALUES ('76', '1', '76');
INSERT INTO `sys_role_menu` VALUES ('77', '1', '77');
INSERT INTO `sys_role_menu` VALUES ('78', '1', '78');
INSERT INTO `sys_role_menu` VALUES ('79', '1', '79');
INSERT INTO `sys_role_menu` VALUES ('80', '1', '80');
INSERT INTO `sys_role_menu` VALUES ('81', '1', '81');
INSERT INTO `sys_role_menu` VALUES ('82', '1', '82');
INSERT INTO `sys_role_menu` VALUES ('83', '1', '83');
INSERT INTO `sys_role_menu` VALUES ('84', '1', '84');
INSERT INTO `sys_role_menu` VALUES ('85', '1', '85');
-- ----------------------------
-- Table structure for `sys_user`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user`;
CREATE TABLE `sys_user` (
  `user_id` bigint(20) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) DEFAULT NULL COMMENT '用户名',
  `name` varchar(100) DEFAULT NULL,
  `password` varchar(50) DEFAULT NULL COMMENT '密码',
  `dept_id` int(20) DEFAULT NULL,
  `email` varchar(100) DEFAULT NULL COMMENT '邮箱',
  `mobile` varchar(100) DEFAULT NULL COMMENT '手机号',
  `status` tinyint(255) DEFAULT NULL COMMENT '状态 0:禁用，1:正常',
  `user_id_create` bigint(255) DEFAULT NULL COMMENT '创建用户id',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB AUTO_INCREMENT=137 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of sys_user
-- ----------------------------
INSERT INTO `sys_user` VALUES ('1', 'admin', '超级管理员', 'd633268afedf209e1e4ea0f5f43228a8', '6', 'admin@example.com', '123456', '1', '1', '2017-08-15 21:40:39', '2017-08-15 21:41:00');
INSERT INTO `sys_user` VALUES ('2', 'test', '普通用户', 'b132f5f968c9373261f74025c23c2222', '6', 'test@test.com', null, '1', '1', '2017-08-14 13:43:05', '2017-08-14 21:15:36');
-- ----------------------------
-- Table structure for `sys_user_role`
-- ----------------------------
DROP TABLE IF EXISTS `sys_user_role`;
CREATE TABLE `sys_user_role` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `user_id` bigint(20) DEFAULT NULL COMMENT '用户ID',
  `role_id` bigint(20) DEFAULT NULL COMMENT '角色ID',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8 COMMENT='用户与角色对应关系';

-- ----------------------------
-- Records of sys_user_role
-- ----------------------------
INSERT INTO `sys_user_role` VALUES ('1', '1', '1');
INSERT INTO `sys_user_role` VALUES ('2', '2', '2');


DROP TABLE IF EXISTS `oauth_access_token`;

CREATE TABLE `oauth_access_token` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `token_id` varchar(255) DEFAULT NULL,
  `token_expired_seconds` int(11) DEFAULT '-1',
  `authentication_id` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  `token_type` varchar(255) DEFAULT NULL,
  `refresh_token_expired_seconds` int(11) DEFAULT '-1',
  `refresh_token` varchar(255) DEFAULT NULL,
  UNIQUE KEY `token_id` (`token_id`),
  UNIQUE KEY `refresh_token` (`refresh_token`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

DROP TABLE IF EXISTS `oauth_client_details`;

CREATE TABLE `oauth_client_details` (
  `client_id` varchar(255) NOT NULL,
  `client_secret` varchar(255) DEFAULT NULL,
  `client_name` varchar(255) DEFAULT NULL,
  `scope` varchar(255) DEFAULT NULL,
  `grant_types` varchar(255) DEFAULT NULL,
  `redirect_uri` varchar(255) DEFAULT NULL,
  `access_token_validity` int(11) DEFAULT '-1',
  `refresh_token_validity` int(11) DEFAULT '-1',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `trusted` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`client_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

INSERT INTO `oauth_client_details` (`client_id`, `client_secret`, `client_name`, `scope`, `grant_types`, `redirect_uri`, `access_token_validity`, `refresh_token_validity`, `create_time`, `trusted`)
VALUES
	('test','test','Test Client','read,write','authorization_code,password,refresh_token,client_credentials','http://localhost:8080/oauth/default_redirect_url',-1,-1,'2018-02-03 03:52:00',0);


DROP TABLE IF EXISTS `oauth_code`;

CREATE TABLE `oauth_code` (
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `code` varchar(255) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  `client_id` varchar(255) DEFAULT NULL,
  UNIQUE KEY `code` (`code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



-- ----------------------------
-- gateway
-- ----------------------------
DROP TABLE IF EXISTS `gateway_api_group`;

CREATE TABLE `gateway_api_group` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT 'api名称',
  `describe` varchar(500) DEFAULT NULL COMMENT 'api描述',
  `backend_host` varchar(255) DEFAULT NULL COMMENT '目标地址',
  `backend_port` varchar(255) DEFAULT NULL COMMENT '目标端口',
  `backend_path` varchar(255) DEFAULT NULL COMMENT '目标url前缀',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='api';


DROP TABLE IF EXISTS `gateway_api`;

CREATE TABLE `gateway_api` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT 'api名称',
  `describe` varchar(500) DEFAULT NULL COMMENT 'api描述',
  `url` varchar(255) DEFAULT NULL COMMENT '请求路径',
  `http_method` varchar(255) DEFAULT NULL COMMENT '提交方式',
  `path` varchar(255) DEFAULT NULL COMMENT '后端请求路径',
  `routes` tinyint(1) DEFAULT NULL COMMENT '是否RPC请求',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `group_id` bigint(20) unsigned NOT NULL COMMENT '分组Id',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_url` (`url`),
  KEY `fk_group` (`group_id`),
  CONSTRAINT `fk_group` FOREIGN KEY (`group_id`) REFERENCES `gateway_api_group` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='api';

 
DROP TABLE IF EXISTS `gateway_api_rpc`;

CREATE TABLE `gateway_api_rpc` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `service_name` varchar(255) DEFAULT NULL COMMENT '服务名',
  `method_name` varchar(100) DEFAULT NULL COMMENT '方法名',
  `service_group` varchar(100) DEFAULT NULL COMMENT '服务组名',
  `service_version` varchar(100) DEFAULT NULL COMMENT '服务版本',
  `proto_context` blob COMMENT 'proto内容',
  `dubbo_param_template` blob COMMENT 'dubbo请求参数类型',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `api_id` bigint(20) unsigned NOT NULL COMMENT 'apiId',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_service` (`service_name`,`method_name`,`service_group`,`service_version`,`api_id`),
  KEY `fk_rpc_api` (`api_id`),
  CONSTRAINT `fk_rpc_api` FOREIGN KEY (`api_id`) REFERENCES `gateway_api` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='rpc服务映射表';


DROP TABLE IF EXISTS `gateway_api_springcloud`;

CREATE TABLE `gateway_api_springcloud` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `instance_id` varchar(100) DEFAULT NULL COMMENT '服务ID',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  `api_id` bigint(20) unsigned NOT NULL COMMENT 'apiId',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_instance` (`instance_id`,`api_id`),
  KEY `fk_sc_api` (`api_id`),
  CONSTRAINT `fk_sc_api` FOREIGN KEY (`api_id`) REFERENCES `gateway_api` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='SpringCloud服务映射表';
 
 
DROP TABLE IF EXISTS `gateway_filter`;

CREATE TABLE `gateway_filter` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `name` varchar(255) DEFAULT NULL COMMENT '组件名称',
  `describe` varchar(500) DEFAULT NULL COMMENT '组件描述',
  `filter_type` varchar(100) DEFAULT NULL,
  `rule` varchar(5000) DEFAULT NULL,
  `api_id` bigint(20) DEFAULT NULL COMMENT 'apiId',
  `group_id` bigint(20) DEFAULT NULL COMMENT 'groupId',
  `gmt_create` datetime DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


-- ----------------------------
-- test data
-- ----------------------------

INSERT INTO `gateway_api_group` (`id`, `name`, `describe`, `backend_host`, `backend_port`, `backend_path`, `gmt_create`, `gmt_modified`)
VALUES
	(1,'服务化','服务化标准分组','','','','2018-02-03 03:52:00','2018-02-03 03:52:00');

INSERT INTO `gateway_api` (`id`, `name`, `describe`, `url`,`http_method`, `path`, `routes`, `gmt_create`, `gmt_modified`, `group_id`)
VALUES
	(1,'grpc测试','grpc','/grpc/user','POST','',2,'2018-05-17 11:59:11','2018-05-17 11:59:11',1),
	(2,'dubbo测试','dubbo','/dubbo/user','POST','',1,'2018-05-18 03:13:17','2018-05-18 03:13:17',1),
	(3,'spring测试','springcloud','/springcloud/user','POST','/user',3,'2018-05-18 09:07:31','2018-05-18 09:07:31',1),
	(4,'drools测试','drools','/drools','POST','/users',3,'2018-05-18 10:49:04','2018-05-18 10:49:04',1),
	(5,'damapping测试','datamapping','/datamapping','POST','/users',3,'2018-05-18 15:30:12','2018-05-18 15:30:12',1);

INSERT INTO `gateway_api_rpc` (`id`, `service_name`, `method_name`, `service_group`, `service_version`, `proto_context`, `dubbo_param_template`, `gmt_create`, `gmt_modified`, `api_id`)
VALUES
	(1,'io.github.tesla.grpc.user.UserService','sayHello','tesla','1.0.0',X'0AF6010A0F757365722F757365722E70726F746F1219696F2E6769746875622E7465736C612E677270632E75736572224D0A0B557365725265717565737412120A046E616D6518012001280952046E616D6512160A066D6F62696C6518022001280952066D6F62696C6512120A0469644E6F180320012809520469644E6F224E0A0C55736572526573706F6E736512120A046E616D6518012001280952046E616D6512160A066D6F62696C6518022001280952066D6F62696C6512120A0469644E6F180320012809520469644E6F42210A19696F2E6769746875622E7465736C612E677270632E75736572420455736572620670726F746F330AE2010A16757365722F75736572736572766963652E70726F746F1219696F2E6769746875622E7465736C612E677270632E757365721A0F757365722F757365722E70726F746F326C0A0B5573657253657276696365125D0A0873617948656C6C6F12262E696F2E6769746875622E7465736C612E677270632E757365722E55736572526571756573741A272E696F2E6769746875622E7465736C612E677270632E757365722E55736572526573706F6E7365220042260A19696F2E6769746875622E7465736C612E677270632E7573657242095573657250726F746F620670726F746F33','','2018-05-17 11:59:11','2018-05-17 11:59:11',1),
	(2,'io.github.tesla.dubbo.user.UserService','sayHello','tesla','1.0.0',NULL,X'5B7B2274797065223A22696F2E6769746875622E7465736C612E647562626F2E706F6A6F2E5573657252657175657374222C2265787072657373696F6E223A22247B6A736F6E5374727D227D5D','2018-05-18 03:13:17','2018-05-18 03:13:17',2);


INSERT INTO `gateway_api_springcloud` (`id`, `instance_id`, `gmt_create`, `gmt_modified`, `api_id`)
VALUES
	(1,'tesla','2018-05-18 09:07:31','2018-05-18 09:07:31',3),
	(2,'tesla','2018-05-18 10:49:04','2018-05-18 10:49:04',4),
	(3,'tesla','2018-05-18 15:30:12','2018-05-18 15:30:12',5);
	

INSERT INTO `gateway_filter` (`id`, `name`, `describe`, `filter_type`, `rule`, `api_id`, `group_id`, `gmt_create`, `gmt_modified`)
VALUES
	(1,'Cookie黑名单','标准的Cookie黑名单','BlackCookieHttpRequestFilter','\\.\\./\n\\:\\$\n\\$\\{\nselect.+(from|limit)\n(?:(union(.*?)select))\nhaving|rongjitest\nsleep\\((\\s*)(\\d*)(\\s*)\\)\nbenchmark\\((.*)\\,(.*)\\)\nbase64_decode\\(\n(?:from\\W+information_schema\\W)\n(?:(?:current_)user|database|schema|connection_id)\\s*\\(\n(?:etc\\/\\W*passwd)\ninto(\\s+)+(?:dump|out)file\\s*\ngroup\\s+by.+\\(\nxwork.methodaccessor\n(?:define|eval|file_get_contents|include|require|require_once|shell_exec|phpinfo|system|passthru|preg_\\w+|execute|echo|print|print_r|var_dump|(fp)open|alert|showmodaldialog)\\(\nxwork\\.methodaccessor\n(gopher|doc|php|glob|file|phar|zlib|ftp|ldap|dict|ogg|data)\\:\\/\njava\\.lang\n\\$_(get|post|cookie|files|session|env|phplib|globals|server)\\[\n',NULL,NULL,'2018-05-18 10:48:00','2018-05-18 10:48:00'),
	(2,'URL参数黑名单','标准的URL参数黑名单','URLParamHttpRequestFilter','\\.\\./\n\\:\\$\n\\$\\{\nselect.+(from|limit)\n(?:(union(.*?)select))\nhaving|rongjitest\nsleep\\((\\s*)(\\d*)(\\s*)\\)\nbenchmark\\((.*)\\,(.*)\\)\nbase64_decode\\(\n(?:from\\W+information_schema\\W)\n(?:(?:current_)user|database|schema|connection_id)\\s*\\(\n(?:etc\\/\\W*passwd)\ninto(\\s+)+(?:dump|out)file\\s*\ngroup\\s+by.+\\(\nxwork.methodaccessor\n(?:define|eval|file_get_contents|include|require|require_once|shell_exec|phpinfo|system|passthru|preg_\\w+|execute|echo|print|print_r|var_dump|(fp)open|alert|showmodaldialog)\\(\nxwork\\.MethodAccessor\n(gopher|doc|php|glob|file|phar|zlib|ftp|ldap|dict|ogg|data)\\:\\/\njava\\.lang\n\\$_(get|post|cookie|files|session|env|phplib|globals|server)\\[\n\\<(iframe|script|body|img|layer|div|meta|style|base|object|input)\n(onmouseover|onerror|onload)\\=\n',NULL,NULL,'2018-05-18 10:48:00','2018-05-18 10:48:00'),
	(3,'URL黑名单','标准的URL黑名单','BlackURLHttpRequestFilter','\\.(svn|git|htaccess|bash_history)\n\\.(bak|inc|old|mdb|sql|backup|java|class)$\n(vhost|bbs|host|wwwroot|www|site|root|hytop|flashfxp).*\\.rar\n(phpmyadmin|jmx-console|jmxinvokerservlet)\njava\\.lang\n/(attachments|upimg|images|css|uploadfiles|html|uploads|templets|static|template|data|inc|forumdata|upload|includes|cache|avatar)/(\\\\w+).(php|jsp)\n',NULL,NULL,'2018-05-18 10:48:00','2018-05-18 10:48:00'),
	(4,'Drools测试','Drools测试','DroolsRequestFilter','package io.github.tesla.gateway.netty.filter.drools\n\nimport io.github.tesla.gateway.netty.filter.help.BodyMapping\nimport io.github.tesla.gateway.netty.filter.help.HeaderMapping\nimport io.github.tesla.gateway.netty.filter.help.DroolsContext\n\n\ndeclare User\n    name : String\n    mobile : String\n    idNo : String\nend\n\nrule \"condition: call userService to judge user is normal\"\nno-loop true\nwhen\n    $body:BodyMapping()\n    $header:HeaderMapping()\n    $context:DroolsContext()\nthen\n    User user = new User();\n    user.setName($body.json(\"$.name\"));\n    user.setMobile($body.json(\"$.mobile\"));\n    user.setIdNo($body.json(\"$.idNo\"));\n    String userInfo = $context.toJSONString(user);\n    String returnUserInfo = $context.callService(\"tesla\",\"/user\",userInfo, \"POST\");\n    User returnUser = $context.parseObject(returnUserInfo,User.class);\n    insert(returnUser);\nend\n\nrule \"condition: judge to jingdong or internal service\"\nno-loop true\nwhen\n     $user:User(name==\"test\",mobile==\"123\")\n     $context:DroolsContext()\nthen\n     $context.setResponse(\"www.baidu.com\");\nend',4,NULL,'2018-05-18 11:00:58','2018-05-18 11:00:58'),
	(5,'datamapping测试','datamapping测试','DataMappingRequestFilter','<#assign json = input.path(\"$\")>\n[\n    {\n      \"name\": \"${json.name}\",\n	  \"mobile\": \"${json.mobile}\",\n	  \"idNo\": \"${json.idNo}\"\n    },\n    {\n      \"name\": \"${json.name}\",\n	  \"mobile\": \"${json.mobile}\",\n	  \"idNo\": \"${json.idNo}\"\n    }\n]',5,NULL,'2018-05-18 15:31:08','2018-05-18 15:31:08');

