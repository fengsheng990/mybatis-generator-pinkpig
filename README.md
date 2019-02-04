# mybatis-generator-pinkpig

#### 介绍
*根据实际项目经验，基于官方MBG扩展一些功能，官方源码未改动，随时可升级官方版本号*

*新增以下插件：*

1. *悲观锁插件（SelectByPkWithLockPlugin）*

2. *索引查询插件（SelectByIndexPlugin），包含唯一索引，普通索引；唯一索引返回单条结果，普通索引返回List多条结果*

3. *example字符串替换替换插件（ReplaceExamplePlugin），包含条件查询类中的exmple，client方法中的exmple，xml中id的exmple串替换*

4. *lombok插件（LombokPlugin），支持@Data，@Builder，@NoArgsConstructor，@AllArgsConstructor注解，可选择使用其中1个或多个*

5. *物理分页插件（），实际项目中使用该插件时需要引入项目中的分页对象Page，分页拦截器*

#### 软件架构
*软件架构说明*


#### 安装教程

1. *下载插件工程mybatis-generator-pinkpig，导入开发工具中*

2. *根据实际需要配置generatorConfig.xml*

3. *更改dateScource.properties为实际的数据库信息*

4. *运行MybatisGeneratorTest*
