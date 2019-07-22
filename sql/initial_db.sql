-- 此文件只是提供查看，项目会自动创建数据库
--  异常信息表
drop table if exists exception;
CREATE TABLE exception
(
  id          INTEGER(500)  NOT NULL,
  useTimes    INTEGER(500)  NULL DEFAULT null,
  isSolution  INTEGER(4)    NULL DEFAULT null,
  name        VARCHAR(800)  NULL DEFAULT null,
  type        VARCHAR(100)  NULL DEFAULT null,
  language    VARCHAR(100)  NULL DEFAULT null,
  answer      VARCHAR(800)  NULL DEFAULT null,
  cause       VARCHAR(800)  NULL DEFAULT null,
  keyWord     VARCHAR(300)  NULL DEFAULT null,
  description VARCHAR(1000) NULL DEFAULT null,
  updateDate  date          NULL DEFAULT NULL,
  remark      VARCHAR(500)  NULL DEFAULT null
);

--  方法表
drop table if exists method;
CREATE TABLE method
(
  id          INTEGER(500) NOT NULL,
  useTimes    INTEGER(50)  NOT NULL,
  isOutOfDate INTEGER(4)   NOT NULL,
  name        VARCHAR(200) NULL DEFAULT null,
  type        VARCHAR(100) NULL DEFAULT null,
  language    VARCHAR(100) NULL DEFAULT null,
  keyWord     VARCHAR(300) NULL DEFAULT null,
  answer      VARCHAR(200) NULL DEFAULT null,
  description VARCHAR(500) NULL DEFAULT null,
  remark      VARCHAR(500) NULL DEFAULT null,
  updateDate  date         NULL DEFAULT NULL
);

--  注册表安装软件信息表
drop table if exists systemInstallMsg;
CREATE TABLE systemInstallMsg
(
  id            INTEGER(500) NOT NULL,
  name          VARCHAR(300) NULL DEFAULT null,
  version       VARCHAR(100) NULL DEFAULT null,
  publisher     VARCHAR(200) NULL DEFAULT null,
  installAddr   VARCHAR(500) NULL DEFAULT null,
  installDate   VARCHAR(10)  NULL DEFAULT null,
  unInstallAddr VARCHAR(500) NULL DEFAULT null,
  updateDate    date         NULL DEFAULT NULL,
  enable        INTEGER(2)   NULL DEFAULT NULL
);


--  Linux命令信息表
drop table if exists linuxCmdMsg;
CREATE TABLE linuxCmdMsg
(
  id             BIGINT(500)  NOT NULL,          -- 主键
  linuxCmdExamId BIGINT(500)  NULL DEFAULT null, -- 关联表主键
  linuxCmd       VARCHAR(50)  NULL DEFAULT null,-- Linux命令
  usages         VARCHAR(100) NULL DEFAULT null,-- 作用
  example        VARCHAR(200) NULL DEFAULT null,-- 用法
  permission     VARCHAR(50)  NULL DEFAULT null,-- 使用权限
  insertDate     date         NULL DEFAULT null,-- 添加方法时间
  updateTimes    INTEGER(5)   NULL DEFAULT null,-- 更新次数
  updateDate     date         NULL DEFAULT NULL,-- 更新方法时间
  enable         INTEGER(2)   NULL DEFAULT NULL
);

--  Linux命令信息实例表
drop table if exists linuxCmdExamMsg;
CREATE TABLE linuxCmdExamMsg
(
  id            BIGINT(500)  NOT NULL,         -- 主键
  parameterNote VARCHAR(500) NULL DEFAULT null,-- 参数说明
  examples      VARCHAR(500) NULL DEFAULT null,-- 多个实例，使用中文；作为换行
  insertDate    date         NULL DEFAULT null,-- 添加方法时间
  updateTimes   INTEGER(5)   NULL DEFAULT null,-- 更新次数
  updateDate    date         NULL DEFAULT NULL,-- 更新方法时间
  enable        INTEGER(2)   NULL DEFAULT NULL
);

--  Note信息实例表,笔记信息,Base64加密
drop table if exists noteMsg;
CREATE TABLE noteMsg
(
  id          BIGINT(100)  NOT NULL,         -- 主键
  note        TEXT(2000)   NULL DEFAULT null,-- 笔记
  name        VARCHAR(100) NULL DEFAULT null,-- 笔记名称
  noteType    VARCHAR(100) NULL DEFAULT null,-- 笔记类型
  explains    VARCHAR(500) NULL DEFAULT null,-- 笔记说明
  insertDate  date         NULL DEFAULT null,-- 添加方法时间
  updateTimes INTEGER(5)   NULL DEFAULT null,-- 更新次数
  updateDate  date         NULL DEFAULT NULL,-- 更新方法时间
  enable      INTEGER(2)   NULL DEFAULT NULL -- 是否启用
);

--  注册表安装软件信息表
drop table if exists skin;
CREATE TABLE skin
(
  id               INTEGER(2) NOT NULL,
  themName         VARCHAR(100) NULL DEFAULT null,
  borderName       VARCHAR(100) NULL DEFAULT null,
  waterMarkName    VARCHAR(100) NULL DEFAULT null,
  buttonShaperName VARCHAR(100) NULL DEFAULT null,
  fontPolicyName   VARCHAR(300)  NULL DEFAULT null,
  titlePainterName VARCHAR(100) NULL DEFAULT null,
  skinName         VARCHAR(100) NULL DEFAULT null,
  updateDate       date         NULL DEFAULT NULL,
  enable           INTEGER(2)   NULL DEFAULT NULL
);

-- SQLite有5个原始的数据类型，被称为存储类。存储类这个词表明了一个值在磁盘上存储的格式，其实就是类型或数据类型的同义词。这5个存储类在表4-6中描述。
--
-- 表 4-6 SQLite存储类
--
--
--
--
-- 名称
--
--
-- 说明
--
--
--
-- INTEGER
--
--
-- 整数值是全数字(包括正和负)。整数可以是1, 2, 3, 4, 6或 8字节。整数的最大范围(8 bytes)是{-9223372036854775808, 0, +9223372036854775807}。SQLite根据数字的值自动控制整数所占的字节数。
--
--
-- 空注：参可变长整数的概念。
--
--
--
-- REAL
--
--
-- 实数是10进制的数值。SQLite使用8字节的符点数来存储实数。
--
--
--
-- TEXT
--
--
-- 文本(TEXT)是字符数据。SQLite支持几种字符编码，包括UTF-8和UTF-16。字符串的大小没有限制。
--
--
--
-- BLOB
--
--
-- 二进制大对象(BLOB)是任意类型的数据。BLOB的大小没有限制。
--
--
--
-- NULL
--
--
-- NULL表示没有值。SQLite具有对NULL的完全支持。
--
--
