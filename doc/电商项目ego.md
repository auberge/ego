# 电商项目Ego

## 第一天 基于SOA架构，使用Dubbo，逆向工程，分页插件完成商品查询

### 一、电商项目介绍

**1.电商行业的几种模式**

1.1B2B：企业到企业，商家到商家。代表：阿里巴巴、慧聪网

1.2B2C：商家到客户，代表：京东、淘宝商城（B2B2C）

1.3C2C：客户到客户，代表：淘宝集市

1.4O2O：线上到线下

**2.技术选型**

2.1spring、springMVC、MyBatis

2.2JSP、JSTL、jquery、jQuery plugin、EasyUI、KindEditor（富文本编辑器）、CSS+DIV

2.3Redis（缓存服务器）

2.4Solr（搜索）

2.5Dubbo（调用系统服务）

2.6Mysql

2.7Nginx（web 服务器）

2.8Jsonp（跨域数据请求格式）

2.9Nexus（Maven 私服）

2.10MyBatis 逆向工程

2.11HttpClient：使用 java 代码完成请求和响应的技术

2.12MyCat：mysql 分库分表技术

**3.开发工具和环境**

3.1Intellij Idea 19.1

3.2Maven 3.6.0

3.3Tomcat 7.0.37

3.4JDK 1.8

3.5MYSQL 5.7

3.6Nginx 1.8.0

3.7Win 10 操作系统

3.8Linux（服务器系统）

**4.人员搭配**

4.1产品经理：3人，确定需求以及给出产品原型图

4.2项目经历：1人，项目管理

4.3前端团队：5人，根据产品经理给出的原型制作静态页面

4.4后端团队：20人，实现产品功能

4.5测试团队：5人，测试所有功能

4.7运维团队：3人，项目的发布以及维护

**5.项目周期**

5.1 6个月

**6.整个电商结构图**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191116213719695.png)

**7.基于 SOA 架构**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191116215827241.png)

### 二、数据库准备和逆向工程

1.直接运行 SQL 脚本

2.使用你想工程生成 mapper 和 pojo

### 三、搭建 maven 环境

**1.为什么使用 Nexus 搭建 maven 私服（私服的作用）**

1.1公司所有开发成员没有外网，通过局域网连接 nexus 私服，由私服连接外网

**2把项目发布到私服，其他人员从私服下载**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191116223114702.png)

**3.搭建 nexus 的步骤**

3.1nexus-2.12.0-01-bundle.zip 解压到任意路径

3.2修改默认端口（默认8081）

3.2.1nexus-2.12.0-01\conf\nexus.properties

3.3粘贴索引库

![image-20191116232835334](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191116232835334.png)

3.3.1先清空 sonatype-work\nexus\indexer\central-ctx 里的内容

3.3.2把解压后的索引文件粘贴到 sonatype-work\nexus\indexer\central-ctx 当中

3.4进入 D:\Server\nexus\nexus-2.12.0-01\bin\jsw\windows-x86-64（对应自己的系统）

3.4.1install-nexus.bat 安装服务

3.4.2start-nexus.bat 启动服务

3.4.3stop-nexus.bat 停止服务

3.4.4uninstall-nexus.bat 卸载服务

3.5在浏览器输入 http://localhost:8091/nexus

3.7点击登录，账号 admin 密码 admin123

3.8在左侧搜索框输入 artifactid 测试是否配置成功

**4.使用maven连接私服**

4.1前提把 maven 项目搭建好，并设置 users settings 引用 settings.xml

4.2在 settings.xml 中配置

4.2.1配置本地仓库

```xml
<localRepository>D:\Server\maven\repository</localRepository>
```

4.2.2修改镜像地址，本地 maven 直接连私服 nexus

```xml
<mirror>
    <id>nexus-releases</id>
    <mirrorOf>*</mirrorOf>
    <name>Human Readable Name for this Mirror.</name>
    <url>http://localhost:8091/nexus/content/groups/public/</url>
</mirror>
<mirror>
    <id>nexus-snapshots</id>
    <mirrorOf>*</mirrorOf>
    <name>Human Readable Name for this Mirror.</name>
    <url>http://localhost:8091/nexus/content/repositories/apache-snapshots/</url>
</mirror>
```

4.2.3设置 jdk 版本

```xml
<profile>
    <id>jdk-1.8</id>
    <activation>
        <activeByDefault>true</activeByDefault>
        <jdk>1.8</jdk>
    </activation>
    <properties>
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <maven.compiler.compilerVersion>1.8</maven.compiler.compilerVersion>
    </properties>
```

4.2.4配置私服构建（连接私服用到的 jar 等内容）

```xml
<profile>
    <id>nexusTest</id>
    <repositories>
        <repository>
            <id>local-nexus</id>
            <url>http://127.0.0.1:8091/nexus/content/groups/public/</url>
            <releases>
                <enabled>true</enabled>
            </releases>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
        </repository>
    </repositories>
</profile>
```

4.2.5配置私服构建生效

4.2.5.1nexusTest 上面 <profile> 的 id

```xml
<activeProfiles>
    <!--激活id为nexusTest的profile-->
    <activeProfile>nexusTest</activeProfile>
  </activeProfiles>
```

**5连接私服** 

5.1在 pom.xml 中配置

```xml
<distributionManagement>
    <repository>
        <id>releases</id>
        <url>http://localhost:8091/nexus/content/repositories/releases/</url>
    </repository>
    <snapshotRepository>
        <id>snapshots</id>
        <url>http://localhost:8091/nexus/content/repositories/snapshots/</url>
    </snapshotRepository>
</distributionManagement>
```

5.2在 settings.xml 中配置

5.2.1 <id> 的值必须和 pom.xml 中 <id> 对应

```xml
<server>  
    <id>releases</id>  
    <username>admin</username> 
    <password>admin123</password>  
</server>  
<server>  
    <id>snapshots</id>  
    <username>admin</username>  
    <password>admin123</password>  
</server> 
```

5.3上传项目

### 四、创建项目

**1.创建六个项目**

1.1ego-commons：放工具类等

1.2ego-manage：后台项目

1.3ego-parent：父项目

1.4ego-pojo：实体类

1.5ego-service：服务接口

1.6ego-service-impl：dubbo 的 provider

**2.把后台页面放到 ego-manage/web-app/WEB-INF 下**

**3.在 ego-manage 编写控制类**

```java
@Controller
public class PageControler {
    @RequestMapping("/")
    public String welcome(){
        return "index";
    }
    @RequestMapping("{page}")
    public String showPage(@PathVariable String page){
        return page;
    }
}
```

### 五、MyBatis 分页插件

**1.在 mybatis 中配置 <plugin> 标签，在程序员所编写的 sql 命令基础上添加一些内容**

**2.在 pom.xml 中配置依赖**

```xml
        <!-- 分页插件 -->
        <dependency>
            <groupId>com.github.pagehelper</groupId>
            <artifactId>pagehelper</artifactId>
            <version>4.1.6</version>
        </dependency>
```

**3.创建 mybatis.xml 并配置信息**

### 六、实现商品分页显示功能

**1.在 ego-commons 中创建 easyuidatagrid 类** 

1.1 把所有 ego-pojo 中类序列化 

**2.在 ego-service 中创建 TbItemDubboService 接口** 

```java
public interface TbItemDubboService {

    /**
     * 商品分页查询
     * page，rows
     */
    EasyUIDataGrid show(int page, int rows);
}
```

**3.在 ego-service-impl 中新建 TbItemDubboServiceImpl 实现类**

```java
public class TbItemDubboServiceImpl implements TbItemDubboService {
    @Resource
    private TbItemMapper tbItemMapper;

    @Override
    public EasyUIDataGrid show(int page, int rows) {
        PageHelper.startPage(page, rows);
        //查询全部
        List<TbItem> list = tbItemMapper.selectByExample(new TbItemExample());

        //分页代码
        //设置分页条件
        PageInfo<TbItem> pi = new PageInfo<>(list);

        //把结果放入到实体类
        EasyUIDataGrid dataGrid = new EasyUIDataGrid();
        dataGrid.setRows(pi.getList());
        dataGrid.setTotal(pi.getTotal());

        return dataGrid;
    }
}
```

**4.在 ego-service-impl 的 applicationContext-dubbo.xml 中配置接口** 

```xml
	<!--商品类目服务-->
	<dubbo:service interface="com.ego.dubbo.service.TbItemDubboService" 		ref="tbItemDubboServiceImpl"/>
	<bean id="tbItemDubboServiceImpl" class="com.ego.dubbo.service.impl.TbItemDubboServiceImpl"/>
```

**5.编写 Test 类运行 dubbo 服务** 

```java
public class Test {
    public static void main(String[] args) {
        Main.main(args);
    }
}
```

**6.在 ego-manage 的 TbItemService 接口及实现类中添加**

```java
public interface TbItemService {
    /**
     * 显示商品
     * page,rows
     */
    EasyUIDataGrid show(int page, int rows);
}

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboServiceimpl;

    @Override
    public EasyUIDataGrid show(int page, int rows) {
        return tbItemDubboServiceimpl.show(page, rows);
    }
}
```

**7.在 ego-manage 中新建 TbItemController 控制器** 

```java
@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceimpl;
    
    /**
     * 分页显示商品
     */
    @RequestMapping("item/list")
    @ResponseBody
    public EasyUIDataGrid show(int page, int rows) {
        return tbItemServiceimpl.show(page, rows);
    }
}
```

### 七、商品上架、下架、删除

**1.在 ego-service 的 TbItemDubboService 接口及实现类中添加方法** 

```java
public interface TbItemDubboService {

    /**
     * 根据id修改状态
     * id，status
     */
    int updItemStatus(TbItem tbItem);
}

public class TbItemDubboServiceImpl implements TbItemDubboService {
    @Resource
    private TbItemMapper tbItemMapper;

    @Override
    public int updItemStatus(TbItem tbItem) {
        return tbItemMapper.updateByPrimaryKeySelective(tbItem);
    }
}

```

**2.在 ego-commons 中添加 EgoResult ，做为 java 代码和 jsp 叫做公共类**

```java
public class EgoResult {
    private int status;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
```

**3.在 ego-manage 的 TbItemService 接口及实现类中添加方法** 

```java
public interface TbItemService {
    /**
     * 批量修改商品状态
     * ids,status
     */
    int update(String ids, byte status);
}
```

**4.在 ego-manage 的 TbItemController 控制器中添加三个方法** 

```java
@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceimpl;

    /**
     * 批量删除商品
     * ids
     */
    @RequestMapping("rest/item/delete")
    @ResponseBody
    public EgoResult delete(String ids) {
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceimpl.update(ids, (byte) 3);
        if (index == 1) {
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 批量上架商品
     * ids
     */
    @RequestMapping("rest/item/reshelf")
    @ResponseBody
    public EgoResult reshelf(String ids) {
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceimpl.update(ids, (byte) 1);
        if (index == 1) {
            egoResult.setStatus(200);
        }
        return egoResult;
    }

    /**
     * 批量下架商品
     * ids
     */
    @RequestMapping("rest/item/instock")
    @ResponseBody
    public EgoResult instock(String ids) {
        EgoResult egoResult = new EgoResult();
        int index = tbItemServiceimpl.update(ids, (byte) 2);
        if (index == 1) {
            egoResult.setStatus(200);
        }
        return egoResult;
    }
}

```

## 第二天 使用VSFTPD和Nginx完成商品新增

### 一、商品类目显示

**1.在 ego-service 中创建 TbItemCatDubboService 接口及实现类**

```java
public interface TbItemCatDubboService {
    /**
     * 根据父类目id查询所有子类目
     * pid
     */
    List<TbItemCat> show(long pid);
}
```

**2.在 ego-service-impl 的 applicationContext-dubbo.xml 中注册发布服务接口**

```xml
	<!--商品类目-->
    <dubbo:service interface="com.ego.dubbo.service.TbItemCatDubboService" ref="tbItemCatDubboServiceImpl"/>
    <bean id="tbItemCatDubboServiceImpl" class="com.ego.dubbo.service.impl.TbItemCatDubboServiceImpl"/>
```

**3.在 ego-commons 中新建一个 EasyUITree 类,专门封装 EasyUI 中 Tree 组件每个节点对象的数据** 

3.1 state 是 EasyUI 中 node 节点 的固 定属性 之一 .可取 值只 有 ”closed” 和 ”open”, 如 果 取 值 为 ”closed” 显 示 节 点 为 文 件 夹 ,如果取值为”open”显示节点为文件

```java
public class TbItemCatDubboServiceImpl implements TbItemCatDubboService {
    @Resource
    private TbItemCatMapper tbItemCatMapper;

    @Override
    public List<TbItemCat> show(long pid) {
        TbItemCatExample example = new TbItemCatExample();
        example.createCriteria().andParentIdEqualTo(pid);
        return tbItemCatMapper.selectByExample(example);
    }
}
```

**4.在 ego-manage 中新建 TbItemCatService 接口及实现类**

```java
public interface TbItemCatService {
    /**
     * 根据父类目id查询所有子菜单
     * pid
     */
    List<EasyUITree> show(long pid);
}
```

**5.在 ego-manage 中新建 TbItemCatController 控制器**

5.1 在控制器方法中设置参数 id 默认值为 0,第一请求时没有参数, 希望查询出所有一级菜单.一级菜单的父 id 为 0 

```java
@Controller
public class TbItemCatController {
    @Resource
    private TbItemCatService tbItemCatServiceImpl;

    /**
     * 显示商品类目
     * id(默认值为0)
     */
    @RequestMapping("item/cat/list")
    @ResponseBody
    public List<EasyUITree> show(@RequestParam(defaultValue = "0") long id) {
        return tbItemCatServiceImpl.show(id);
    }
}
```

### 二、实现商品新增 - 图片上传

**1.需要在 linux 中安装 vsftpd，安装后实现使用 ftpClient 完成图片上传功能**

**2.在 vsftpd 所在服务器安装 nginx，实现图片回显**

**3.在 ego-parent 中 pom.xml 中引入 ftpclient 的 jar**

```xml
 <!--文件上传-->
            <dependency>
                <groupId>commons-net</groupId>
                <artifactId>commons-net</artifactId>
                <version>3.3</version>
            </dependency>
```

**4.在 ego-commons 中 pom.xml 中引入 ftpclient 的 jar**

```xml
    <dependencies>
        <dependency>
            <groupId>commons-net</groupId>
            <artifactId>commons-net</artifactId>
        </dependency>
    </dependencies>
```

**5.在 ego-commons 中 com.ego.commons.utils 添加 ftpUtil 工具类**

![image-20191122135257406](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191122135257406.png)

**6.在 ego-commons 中添加 commons.properties 文件用于存放文件服务器参数**

```properties
ftpclient.host = 192.168.18.135
ftpclient.port = 21
ftpclient.username = ftpuser
ftpclient.password = ftpuser
ftpclient.basePath = /home/ftpuser/
ftpclient.filePath = /
```

**7.在 ego-commons 的 applicationContext-spring 中添加属性文件扫描**

```xml
<!--整个项目，此标签最多只能出现一次-->
	<context:property-placeholder location="classpath:commons.properties"/>
```

**8.在 ego-manage 中新建 PicService 接口及实现类**

8.1方法返回值是map原因

8.1.1jsp中使用kindeditor的多文件上传插件，要求返回值为固定格式，成功和失败返回值不一样，不建议新建一个类

```java
public interface PicService {
    /**
     * 文件上传
     * file
     */
    Map<String, Object> upload(MultipartFile file);
}

@Service
public class PicServiceImpl implements PicService {
    @Value("${ftpclient.host}")
    private String host;
    @Value("${ftpclient.port}")
    private int port;
    @Value("${ftpclient.username}")
    private String username;
    @Value("${ftpclient.password}")
    private String password;
    @Value("${ftpclient.basePath}")
    private String basePath;
    @Value("${ftpclient.filePath}")
    private String filePath;

    @Override
    public Map<String, Object> upload(MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        boolean result = false;

        String oldName = file.getOriginalFilename();
        String fileName = UUID.randomUUID().toString() + oldName.substring(oldName.lastIndexOf("."));
        try {
            result = FtpUtil.uploadFile(host, port, username, password, basePath, filePath, fileName, file.getInputStream());
            System.out.println(result);
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
           if (result){
               map.put("error",0);
               map.put("url","http://"+host+":80"+filePath+fileName);
           }else {
               map.put("error",1);
               map.put("msg","图片上传失败");
           }
        }
        return map;
    }
}
```

**9.在 ego-manage 中新建 PicController 控制器**

```java
@Controller
public class PicController {
    @Resource
    private PicService picService;

    /**
     * 文件上传
     * uploadFile
     */
    @RequestMapping("/pic/upload")
    @ResponseBody
    public Map<String, Object> upload(MultipartFile uploadFile) {
        return picService.upload(uploadFile);
    }
}
```

### 三、实现商品新增

**1.在 ego-service 的 TbItemDubboService 接口及实现类中添加方法**

1.1实现类中try catch原因：当出错时继续运行

1.2通过手动抛出异常可以使用dubbo的provider和consumer交互的问题

```java
public interface TbItemDubboService {
    /**
     * 新增包含商品表和商品描述表表
     * tbItem,tbItemDesc
     */
    int insTbItemDesc(TbItem tbItem, TbItemDesc tbItemDesc) throws Exception;

}

public class TbItemDubboServiceImpl implements TbItemDubboService {
    @Resource
    private TbItemMapper tbItemMapper;

    @Resource
    private TbItemDescMapper tbItemDescMapper;

    @Override
    public int insTbItemDesc(TbItem tbItem, TbItemDesc tbItemDesc) throws Exception {
        int index = 0;
        try {
            index = tbItemMapper.insertSelective(tbItem);
            index += tbItemDescMapper.insertSelective(tbItemDesc);
        }catch (Exception e){
            e.getStackTrace();
        }
        if (index == 2) {
            return 1;
        } else {
            throw new Exception("新增失败，数据回滚！");
        }
    }
}
```

**2.在 ego-service-impl 的 applicationContext-spring.xml 中添加 ins 事务回滚**

```xml
	<tx:method name="ins*" rollback-for="java.lang.Exception"/>
```

**3.在 ego-manage 的 TbItemService 接口及实现类中添加方法**

3.1方法没有处理异常，因为希望在控制器中捕获异常信息

```java
public interface TbItemService {
    /**
     * 商品新增
     * item,desc
     */
    int saveTrans(TbItem item, String desc) throws Exception;
}

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboServiceimpl;
    
    @Reference
    private TbItemDescDubboService tbItemDescDubboServiceImpl;

    @Override
    public int saveTrans(TbItem item, String desc) throws Exception {
        long id = IDUtils.genItemId();
        item.setId(id);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        item.setStatus((byte) 1);

        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(id);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);

        return tbItemDubboServiceimpl.insTbItemDesc(item, itemDesc);
    }
}
```

**4.在 ego-commons 中 EgoResult 实体类中添加 Object data；属性**

```java
public class EgoResult {
    private int status;
    private Object data;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
```

**5.在 ego-manage 的 TbItemController 控制器中添加方法**

```java
@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceimpl;

    /**
     * 商品新增
     * item,desc(描述）
     */
    @RequestMapping("item/save")
    @ResponseBody
    public EgoResult insert(TbItem item, String desc) {
        EgoResult result = new EgoResult();
        int index = 0;
        try {
            index = tbItemServiceimpl.saveTrans(item, desc);
        } catch (Exception e) {
            result.setData(e.getMessage());
        }
        if (index == 1) {
            result.setStatus(200);
        }
        return result;
    }
}
```

**6.在 item-add.jsp 中添加错误信息提示**

```js
 $.post("/item/save", $("#itemAddForm").serialize(), function (data) {
            if (data.status == 200) {
                $.messager.alert('提示', '新增商品成功!');
            } else {
                $.messager.alert('提示', '新增商品失败!<br/>原因：' + data.data);
            }
        });
```

## 第三天 基于json格式完成商品规格参数管理

### 一、规格参数管理

**1.优化规格参数数据库设计**

**2.需求：**

2.1每个分类有不同的分类模板

2.2根部模板每个商品有具体信息

**3.传统数据库设计**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191122172700976.png)

**4.优化数据库设计**

4.1前提：group和子项的对应关系是比较灵活的

4.2商品_子项表中数据比较多

4.3优化后：

4.3.1所有规格参数模板设置到一个列中，这个列中存储特定格式的数据，把数据存储为json数据格式

4.3.2设置一个表，设置某个列存储json数据格式

4.3.2.1目的：存储每个商品的具体规格信息

### 二、规格参数查询实现

**1.在 ego-service 的 TbItemCatDubboService 接口及实现类中添加方法**

```java
 	/**
     * 根据类目id查询
     * id
     */
    TbItemCat selById(long id);

	@Override
    public TbItemCat selById(long id) {
        return tbItemCatMapper.selectByPrimaryKey(id);
    }
```

**2.在 ego-service 中新建 TbItemParamDubboService 接口及实现类**

2.1带有withBOLBs()的方法会查询text类型的列

```java
public interface TbItemParamDubboService {
    /**
     * 分页查询数据
     * page,rows
     * return 当前页显示数据和总条数
     */
    EasyUIDataGrid showPage(int page, int rows);
}

public class TbItemParamDubboServiceImpl implements TbItemParamDubboService {
    @Resource
    private TbItemParamMapper tbItemParamMapper;

    @Override
    public EasyUIDataGrid showPage(int page, int rows) {
        //设置分页条件
        PageHelper.startPage(page,rows);
        //设置查询的sql语句
        //xxxExample()设置了什么，相当于sql中where从句中添加条件

        //如果表中有一个或一个以上的列是text类型，生成的方法xxxxWithBolbs（）
        //如果使用xxxxxWithBolbs（）查询结果中包含带有text类的列，如果没有使用withBolbs（）方法不带有text类型
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(new TbItemParamExample());
        //根据程序员自己编写sql语句结合分页插件产生最终结果，封装到PageInfo中
        PageInfo<TbItemParam> pi = new PageInfo<>(list);

        //设置方法返回结果
        EasyUIDataGrid dataGrid=new EasyUIDataGrid();
        dataGrid.setRows(pi.getList());
        dataGrid.setTotal(pi.getTotal());
        return dataGrid;
    }
}
```

**3.在 ego-service-impl 的 applicationContext-dubbo.xml 中注册服务接口**

```xml
	<!--商品规格参数-->
    <dubbo:service interface="com.ego.dubbo.service.TbItemParamDubboService" ref="tbItemParamDubboServiceImpl"/>
    <bean id="tbItemParamDubboServiceImpl" class="com.ego.dubbo.service.impl.TbItemParamDubboServiceImpl"/>
```

**4.在 ego-manage 中新建 TbItemParamChild 实体类,封装 jsp 中要的所有数据**

```java
public class TbItemParamChild extends TbItemParam {
    private String itemCatName;

    public String getItemCatName() {
        return itemCatName;
    }

    public void setItemCatName(String itemCatName) {
        this.itemCatName = itemCatName;
    }
}
```

**5.在 ego-manage 中新建 TbItemParamService 接口及实现类**

```java
public interface TbItemParamService {
    /**
     * 显示商品规格参数
     * page,rows
     * */
    EasyUIDataGrid showPage(int page,int rows);
}


@Service
public class TbItemParamServiceImpl implements TbItemParamService {
    @Reference
    private TbItemParamDubboService tbItemParamDubboServiceImpl;

    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;

    @Override
    public EasyUIDataGrid showPage(int page, int rows) {
        EasyUIDataGrid dataGrid = tbItemParamDubboServiceImpl.showPage(page, rows);
        List<TbItemParam> list = (List<TbItemParam>) dataGrid.getRows();
        List<TbItemParamChild> childList = new ArrayList<>();
        for (TbItemParam param : list) {
            TbItemParamChild child = new TbItemParamChild();

            child.setId(param.getId());
            child.setItemCatId(param.getItemCatId());
            child.setParamData(param.getParamData());
            child.setItemCatName(tbItemCatDubboServiceImpl.selById(child.getItemCatId()).getName());
            child.setCreated(param.getCreated());
            child.setUpdated(param.getUpdated());
            childList.add(child);
        }
        dataGrid.setRows(childList);
        return dataGrid;
    }
}
```

**6.在 ego-manage 中新建 TbItemParamController 控制器**

```java
@Controller
public class TbItemParamController {
    @Resource
    private TbItemParamService tbItemParamServiceimpl;

    /**
     * 规格参数分页显示
     * page,rows
     */
    @RequestMapping("item/param/list")
    @ResponseBody
    public EasyUIDataGrid showPage(int page, int rows) {
        return tbItemParamServiceimpl.showPage(page, rows);
    }
}
```

### 三、规格参数批量删除

**1.在 ego-service 中 TbItemParamDubboService 接口及实现类添加方法**

```java
	/**
     * 批量删除规格参数
     * ids
     * */
    int delByIds(String ids) throws Exception;

 	@Override
    public int delByIds(String ids) throws Exception {
        int index = 0;
        String[] idStr = ids.split(",");
        for (String id : idStr) {
            index += tbItemParamMapper.deleteByPrimaryKey(Long.parseLong(id));
        }
        if (index==idStr.length){
            return 1;
        }else {
            throw new Exception("删除失败，原因：数据可能不存在");
        }
    }
```

**2.在 ego-service-impl 的 applicationContext-dubbo.xml 中添加 rollback-for 删除回滚**

```xml
    <tx:method name="del*" rollback-for="java.lang.Exception"/>
```

**3.在 ego-manage 中 TbItemParamService 接口及实现类中添加方法**

```java
	/**
     * 批量删除规格参数
     * ids
     */
    int delete(String ids) throws Exception;

	@Override
    public int delete(String ids) throws Exception {
        return tbItemParamDubboServiceImpl.delByIds(ids);
    }
```

**4.在 ego-manage 中 TbItemParamController 控制器中添加方法**

```java
    /**
     * 批量删除规格参数
     * ids
     */
    @RequestMapping("item/param/delete")
    @ResponseBody
    public EgoResult delete(String ids) {
        EgoResult result = new EgoResult();
        try {
            int index = tbItemParamServiceimpl.delete(ids);
            if (index == 1) {
                result.setStatus(200);
            }
        } catch (Exception e) {
            result.setData(e.getMessage());
        }
        return result;
    }
}
```

**5. 在ego-manage 的 item-param-list.jsp 中修改代码**

```js
if (data.status == 200) {
    $.messager.alert('提示', '删除商品规格成功!', undefined, function () {
        $("#itemParamList").datagrid("reload");
    });
}else {
    $.messager.alert('提示', '删除商品规格失败!<br/>'+data.data, undefined, function () {
        $("#itemParamList").datagrid("reload");
    });
}
```

### 四、规格参数新增

**1.在 ego-service 的 TbItemParamDubboService 接口及实现类中添加方法**

```java
	/**
     * 分局类目id查询参数模板
     * catId
     */
    TbItemParam selByCatId(long catId);

	@Override
    public TbItemParam selByCatId(long catId) {
        TbItemParamExample example = new TbItemParamExample();
        example.createCriteria().andItemCatIdEqualTo(catId);
        List<TbItemParam> list = tbItemParamMapper.selectByExampleWithBLOBs(example);
        if (list!=null&&list.size()>0)
            //要求每个类目只能有一个模板
            return list.get(0);
        return null;
    }
```

**2.在 ego-manage 的 TbItemParamService 接口及实现类中添加方法**

```java
	/**
     * 根据类目查询模板信息
     * catId
     */
    EgoResult showParam(long catId);

	@Override
    public EgoResult showParam(long catId) {
        EgoResult result=new EgoResult();
        TbItemParam param = tbItemParamDubboServiceImpl.selByCatId(catId);
        if (param!=null){
            result.setStatus(200);
            result.setData(param);
        }
        return result;
    }
```

**3.在 ego-manage 的 TbItemParamController 控制器中添加方法**

```java
	/**
     * 点击商品类目按钮显示添加分组按钮
     * 判断是否已经添加模板
     * catId
     * */
    @RequestMapping("item/param/query/itemcatid/{catId}")
    @ResponseBody
    public EgoResult show(@PathVariable long catId) {
        return tbItemParamServiceimpl.showParam(catId);
    }
```

**4.在 ego-service 的 TbItemParamDubboService 接口及实现类中添加方法**

```java
	/**
     * 新增规格参数（支持主键自增）
     * param
     */
    int insParam(TbItemParam param);
	@Override
    public int insParam(TbItemParam param) {
        return tbItemParamMapper.insertSelective(param);
    }
```

**5.在 ego-manage 的 TbItemParamService 接口及实现类中添加方法**

```java
	/**
     * 新增规格模板信息
     * param
     * @return
     */
    EgoResult save(TbItemParam param);

	@Override
    public EgoResult save(TbItemParam param) {
        EgoResult result=new EgoResult();
        Date date=new Date();
        param.setCreated(date);
        param.setUpdated(date);
        int index = tbItemParamDubboServiceImpl.insParam(param);
        if (index>0){
            result.setStatus(200);
        }
        return result;
    }
```

**6.在 ego-manage 的 TbItemParamController 控制器中添加方法**

```java
	/**
     * 添加规格参数模板
     * parame，catId
     * */
    @RequestMapping("item/param/save/{catId}")
    @ResponseBody
    public EgoResult save(TbItemParam param,@PathVariable long catId) {
        param.setItemCatId(catId);
        return tbItemParamServiceimpl.save(param);
    }
```

### 五、商品新增同时新增商品规格信息

**1.在 ego-service 的 TbItemDubboService 接口及实现类中修改方法**

```java
	/**
     * 新增包含商品表和商品描述表表
     * tbItem,tbItemDesc
     */
    int insTbItemDesc(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem paramItem) throws Exception;

	@Override
    public int insTbItemDesc(TbItem tbItem, TbItemDesc tbItemDesc, TbItemParamItem paramItem) throws Exception {
        int index = 0;
        try {
            index = tbItemMapper.insertSelective(tbItem);
            index += tbItemDescMapper.insertSelective(tbItemDesc);
            index += tbItemParamItemMapper.insertSelective(paramItem);
        } catch (Exception e) {
            e.getStackTrace();
        }
        if (index == 3) {
            return 1;
        } else {
            throw new Exception("新增失败，数据回滚！");
        }
    }
```

**2.在 ego-manage 的 TbItemService 接口及实现类中修改方法**

```java
	/**
     * 商品新增
     * item,desc
     */
    int saveTrans(TbItem item, String desc, String ParamItem) throws Exception;

	@Override
    public int saveTrans(TbItem item,String desc,String itemParam)throws Exception {
        long id = IDUtils.genItemId();
        item.setId(id);
        Date date = new Date();
        item.setCreated(date);
        item.setUpdated(date);
        item.setStatus((byte) 1);

        TbItemDesc itemDesc = new TbItemDesc();
        itemDesc.setItemDesc(desc);
        itemDesc.setItemId(id);
        itemDesc.setCreated(date);
        itemDesc.setUpdated(date);

        TbItemParamItem paramItem=new TbItemParamItem();
        paramItem.setCreated(date);
        paramItem.setUpdated(date);
        paramItem.setItemId(id);
        paramItem.setParamData(itemParam);

        return tbItemDubboServiceimpl.insTbItemDesc(item, itemDesc, paramItem);
    }
```

**3.在 ego-manage 的 TbItemController 控制器中修改方法**

```java
    /**
     * 商品新增
     * item,desc(描述）
     */
    @RequestMapping("item/save")
    @ResponseBody
    public EgoResult insert(TbItem item, String desc,String itemParams) {
        EgoResult result = new EgoResult();
        int index = 0;
        try {
            index = tbItemServiceimpl.saveTrans(item, desc,itemParams);
        } catch (Exception e) {
            //e.printStackTrace();
            result.setData(e.getMessage());
        }
        if (index == 1) {
            result.setStatus(200);
        }
        return result;
    }
```

## 第四天 使用jsonp完成前后台首页导航菜单

### 一、门户导航菜单功能需求分析

**1.在 ego-portal 中显示 tb_item_cat 商品类目的数据，让 ego-portal 调用 ego-item中 的数据，有 ego-item 调用 dubbo**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191123160049726.png)

**2.跨域**

2.1一个 Servlet 容器（tomcat）中的项目调用另一个 Servlet 容器（tomcat）中的项目，称为跨域

**3.ajax在研发时出于安全性考虑是不支持跨域请求的**

3.1解决办法：使用 jsonp

3.2解决办法：有 ego-portal 访问自己的控制器，自己的控制器访问自己的 service ，在自己的 service 中使用 httpclient 调用 ego-item 的控制器方法

### 二、jsonp

**1.jsonp 跨域 ajax 数据请求的解决方案**

**2.发展由来**

2.1ajax 不能进行跨域请求（如果 ajax 请求的控制器返回的就是字符串或 json 数据，不能访问）

2.2发现可以在一个项目中直接访问另一个项目的js文件（标签引用还是通过 ajax 访问）

2.3使用 ajax 请求另一个项目的控制器，把控制器返回的结果伪装成 js 文件

**3.使用 jsonp 时服务器端返回的数据格式**

```ajax
函数名（返回的数据）；
```

**4.在客户端编写代码** 

4.1dataType: 一定要设置 jsonp 

4.2jsonp: 传递给服务器的参数名.省略的默认 callback 

4.3jsonCallback: 参数名对应的值,表示最终回调的函数名.省略的默认值 jquery: 一堆数字 

4.4如果直接使用 success:function() 对 jsonpCallback 值没有要求. 

4.5如果单独编写了一个 function ，必须要求 jsonpCallback 和 function 的名称相同 

```js
$(function(){
    $("button").click(function(){ 
        $.ajax({ 
            url:'http://localhost:9002/demo2', 
            type:'post',
            dataType:'jsonp', 
            jsonpCallback:'ab12312321c',
            jsonp:'callback', 
            success:function(data){ 
                alert(data+"success"); 
            } 
        });
    })
})
```

**5.在服务器端添加代码** 

5.1 使用 spring 对 jackson 封装类 MappingJacksonValue 

```java
@Controller 
public class DemoController {
    @RequestMapping("demo3") 
    @ResponseBody
    public MappingJacksonValue demo(String callback){ 
        People p = new People(); 
        p.setId(1);
        p.setName("张三");
        //把构造方法参数转换为 json 字符串并当作最终返回值函数 的参数
        MappingJacksonValue mjv = new MappingJacksonValue(p);
        //最终返回结果中函数名
        mjv.setJsonpFunction(callback); 
        return mjv;
    }
}
```

### 三、搭建 ego-portal 和 ego-item

1.环境和 ego-manage 相同 

2.ego-portal 的端口 8082 , ego-item 的端口是 8081 

3.修改 applicationContext-dubbo.xml 注解扫描

### 四、编写 ego-item 代码

**1.在 ego-item 中新建 PortalMenu 实体类**

1.1最终 ego-item 返回给 ego-portal 的 jsonp 数据中函数的参数值

```java
public class PortalMenu {
    private List<Object> data;

    ……
}
```

**2.在 ego-item 中新建 PortalMenuNode 实体类**

2.1如果菜单是父菜单,当前菜单的数据都封装到这个类中

```java
public class PortalMenuNode {
    private String u;
    private String n;
    private List<Object> i;

   	……
}
```

**3.在 ego-item 中新建 TbItemCatService 接口及实现类**

```java
public interface TbItemCatService {
    /**
     * 查询出所有类目并转换成指定类型
     */
    PortalMenu showCatMenu();
}

@Service
public class TbItemCatServiceImpl implements TbItemCatService {
    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;

    @Override
    public PortalMenu showCatMenu() {
        //查询出所有的一级菜单
        List<TbItemCat> list = tbItemCatDubboServiceImpl.show(0);

        PortalMenu menu = new PortalMenu();
        menu.setData(selAllMenu(list));
        return menu;
    }

    /**
     * 最终返回结果所有查询到的结果
     * list
     */
    public List<Object> selAllMenu(List<TbItemCat> list) {
        List<Object> nodeList = new ArrayList<>();
        for (TbItemCat item : list) {
            if (item.getIsParent()) {
                PortalMenuNode node = new PortalMenuNode();
                node.setN("/products/" + item.getId() + ".html");
                node.setN("<a href='/products/" + item.getId() + ".html'>" + item.getName() + "</a>");
                node.setI(selAllMenu(tbItemCatDubboServiceImpl.show(item.getId())));
                nodeList.add(node);
            } else {
                nodeList.add("/products/"+ item.getId() +".html|" + item.getName());
            }
        }
        return nodeList;
    }
}
```

**4.在 ego-item 中新建 TbItemCatController 控制器**

```java
@Controller
public class TbItemCatController {
    @Resource
    private TbItemCatService tbItemCatServiceImpl;
    
    /**
     * 返回jsonp数据格式包含所有菜单信息
     * callback
     */
    @RequestMapping("rest/itemcat/all")
    @ResponseBody
    public MappingJacksonValue showMenu(String callback) {
        PortalMenu portalMenu = tbItemCatServiceImpl.showCatMenu();
        MappingJacksonValue mjv = new MappingJacksonValue(portalMenu);
        mjv.setJsonpFunction(callback);
        return mjv;
    }
}
```

## 第五天 完成CMS系统

### 一、内容分类查询

**1.在 ego-service 中创建 TbContentCategoryDubboService 接口及实现类**

```java
public interface TbContentCategoryDubboService {
    /**
     * 根据父类目id查询子类目
     * id
     */
    List<TbContentCategory> selById(long id);
}

public class TbContentCategoryDubboServiceImpl implements TbContentCategoryDubboService {
    @Resource
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<TbContentCategory> selById(long id) {
        TbContentCategoryExample example=new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(id).andStatusEqualTo(1);
        return tbContentCategoryMapper.selectByExample(example);
    }
}
```

**2.在 ego-service-impl 的 applicationContext-dubbo.xml 注册发布服务接口**

```xml
	<!--内容分类-->
    <dubbo:service interface="com.ego.dubbo.service.TbContentCategoryDubboService" ref="tbContentCategoryDubboServiceImpl"/>
    <bean id="tbContentCategoryDubboServiceImpl" class="com.ego.dubbo.service.impl.TbContentCategoryDubboServiceImpl"/>
```

**3.在 ego-manage 中新建 TbContentCategoryService 接口及实现类**

```java
public interface TbContentCategoryService {
    /**
     * 查询所有类目并转换成easyUITree的属性要求
     * id
     */
    List<EasyUITree> showCatefory(long id);
}

@Service
public class TbContentCategoryServiceImpl implements TbContentCategoryService {
    @Reference
    private TbContentCategoryDubboService tbContentCategoryDubboServiceImpl;

    @Override
    public List<EasyUITree> showCatefory(long id) {
        List<EasyUITree> treeList=new ArrayList<>();
        List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selById(id);
        for (TbContentCategory category:list){
            EasyUITree tree=new EasyUITree();
            tree.setId(category.getId());
            tree.setState(category.getIsParent()?"closed":"open");
            tree.setText(category.getName());
            treeList.add(tree);
        }
        return treeList;
    }
}
```

**4.在 ego-manage 中新建 TbContentCategoryController 控制器**

```java
@Controller
public class TbContentCategoryController {
    @Resource
    private TbContentCategoryService tbContentCategoryServiceImpl;

    /**
     * 查询显示内容类目
     * id
     */
    @RequestMapping("content/category/list")
    @ResponseBody
    public List<EasyUITree> showCategory(@RequestParam(defaultValue = "0") long id) {
        return tbContentCategoryServiceImpl.showCatefory(id);
    }
}
```

### 二、内容分类新增功能

**1.在 ego-service 的 TbContentCategoryDubboService 接口及实现类中添加方法**

```java
    /**
     * 新增
     * category
     * */
    int insTbContentCategory(TbContentCategory category);

    /**
     * 根据id修改isParent属性
     * id,isParent
     * */
    int updIsParentById(TbContentCategory category);

    @Override
    public int insTbContentCategory(TbContentCategory category) {
        return tbContentCategoryMapper.insertSelective(category);
    }

    @Override
    public int updIsParentById(TbContentCategory category) {
        return tbContentCategoryMapper.updateByPrimaryKeySelective(category);
    }
```

**2.在 ego-manage 的 TbContentCategoryService 接口及实现类中添加方法**

```java
    /**
     * 类目新增
     * category
     * */
    EgoResult create(TbContentCategory category);

    @Override
    public EgoResult create(TbContentCategory category) {
        EgoResult result=new EgoResult();
        //判断当前节点名称是否已经存在
        List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selById(category.getParentId());
        for (TbContentCategory child:children){
            if (child.getName().equals(category.getName())){
                result.setData("该分类名称已存在");
                return result;
            }
        }
        long id= IDUtils.genItemId();
        Date date=new Date();
        category.setCreated(date);
        category.setUpdated(date);
        category.setStatus(1);
        category.setId(id);
        category.setSortOrder(1);
        category.setIsParent(false);
        int index = tbContentCategoryDubboServiceImpl.insTbContentCategory(category);
        if (index>0){
            TbContentCategory parent=new TbContentCategory();
            parent.setId(category.getParentId());
            parent.setIsParent(true);
            index += tbContentCategoryDubboServiceImpl.updIsParentById(parent);
        }
        if (index==2) {
            Map<String, Long> map = new HashMap<>();
            map.put("id", id);
            result.setStatus(200);
            result.setData(map);
        }
        return result;
    }
```

**3.在 ego-manage 的 TbContentCategoryController 控制器中添加方法**

```java
    /**
     * 新增内容类目
     * category
     */
    @RequestMapping("content/category/create")
    @ResponseBody
    public EgoResult create(TbContentCategory category) {
        return tbContentCategoryServiceImpl.create(category);
    }
```

### 三、重命名内容分类

**1.在 ego-service 的 TbContentCategoryDubboService 接口及实现类中添加方法**

```java
    /**
     * 根据id查询内容类目详细信息
     * id
     * */
    TbContentCategory selById(long id);

    @Override
    public TbContentCategory selById(long id) {
        return tbContentCategoryMapper.selectByPrimaryKey(id);
    }
```

**2.在 ego-manage 的 TbContentCategoryService 接口及实现类中添加方法**

```java
    /**
     * 类目修改/重命名
     * category
     * */
    EgoResult update(TbContentCategory category);  

	@Override
    public EgoResult update(TbContentCategory category) {
        EgoResult result = new EgoResult();
        //查询当前节点信息
        TbContentCategory contentCategory = tbContentCategoryDubboServiceImpl.selById(category.getId());
        //查询当前节点的父节点的所有子节点信息
        List<TbContentCategory> children = tbContentCategoryDubboServiceImpl.selByParentId(contentCategory.getParentId());
        for (TbContentCategory child : children) {
            if (child.getName().equals(category.getName())) {
                result.setData("该分类名称已存在");
                return result;
            }
        }
        int index = tbContentCategoryDubboServiceImpl.updTbContentCategory(category);
        if (index > 0) {
            result.setStatus(200);
        }
        return result;
    }
```

**3.在 ego-manage 的 TbContentCategoryController 控制器中添加方法**

```java
    /**
     * 重命名类目名称
     * category
     * */
    @RequestMapping("content/category/update")
    @ResponseBody
    public EgoResult update(TbContentCategory category){
        return tbContentCategoryServiceImpl.update(category);
    }
```

### 四、内容分类删除功能

**1.在 ego-manage 的 TbContentCategoryService 接口及实现类中添加方法**

```java
	/**
     * 删除类目
     * category
     */
    EgoResult delete(TbContentCategory category);

    @Override
    public EgoResult delete(TbContentCategory category) {
        EgoResult result = new EgoResult();
        category.setStatus(2);
        int index = tbContentCategoryDubboServiceImpl.updTbContentCategory(category);
        if (index > 0) {
            TbContentCategory contentCategory = tbContentCategoryDubboServiceImpl.selById(category.getId());
            List<TbContentCategory> list = tbContentCategoryDubboServiceImpl.selByParentId(contentCategory.getParentId());
            if (list == null || list.size() == 0) {
                TbContentCategory parent = new TbContentCategory();
                parent.setId(contentCategory.getParentId());
                parent.setIsParent(false);
                index += tbContentCategoryDubboServiceImpl.updTbContentCategory(parent);
                if (index > 1) {
                    result.setStatus(200);
                }
            }else {
                result.setStatus(200);
            }
        }
        return result;
    }
```

**2.在 ego-manage 的 TbContentCategoryController 控制器中添加方法**

```java
    /**
     * 删除类目
     * category
     */
    @RequestMapping("content/category/delete")
    @ResponseBody
    public EgoResult delete(TbContentCategory category) {
        return tbContentCategoryServiceImpl.delete(category);
    }
```

### 五、内容查询

**1.在 ego-service 中创建 TbContentDubboService 接口及实现类**

```java
public interface TbContentDubboService {
    /**
     * 分页显示查询
     * categoryId,page,rows
     */
    EasyUIDataGrid selContentByPage(long categoryId, int page, int rows);
}

public class TbContentDubboServiceImpl implements TbContentDubboService {
    @Resource
    private TbContentMapper tbContentMapper;
    @Override
    public EasyUIDataGrid selContentByPage(long categoryId, int page, int rows) {
        PageHelper.startPage(page,rows);
        TbContentExample example=new TbContentExample();
        if (categoryId!=0){
            example.createCriteria().andCategoryIdEqualTo(categoryId);
        }
        List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
        PageInfo<TbContent> info=new PageInfo<>(list);
        EasyUIDataGrid dataGrid=new EasyUIDataGrid();
        dataGrid.setTotal(info.getTotal());
        dataGrid.setRows(info.getList());
        return dataGrid;
    }
}
```

**2.在 ego-service-impl 的 applicationContext-dubbo.xml 注册发布服务接口**

```xml
    <!--内容分类-->
	<dubbo:service interface="com.ego.dubbo.service.TbContentDubboService"
                   ref="bContentDubboServiceImpl"/>
    <bean id="bContentDubboServiceImpl" class="com.ego.dubbo.service.impl.TbContentDubboServiceImpl"/>

```

**3.在 ego-manage 中新建 TbContentCategoryService 接口及实现类**

```java
public interface TbContentService {
    /**
     * 分页显示内容信息
     * categoryId,page,rows
     */
    EasyUIDataGrid showContent(long categoryId, int page, int rows);
}

@Service
public class TbContentServiceImpl implements TbContentService {
    @Reference
    private TbContentDubboService tbContentDubboServiceImpl;

    @Override
    public EasyUIDataGrid showContent(long categoryId, int page, int rows) {
        return tbContentDubboServiceImpl.selContentByPage(categoryId, page, rows);
    }
}
```

**4.在 ego-manage 中新建 TbContentCategoryController 控制器**

```java
@Controller
public class TbContentCategoryController {
    @Resource
    private TbContentCategoryService tbContentCategoryServiceImpl;

    /**
     * 查询显示内容类目
     * id
     */
    @RequestMapping("content/category/list")
    @ResponseBody
    public List<EasyUITree> showCategory(@RequestParam(defaultValue = "0") long id) {
        return tbContentCategoryServiceImpl.showCatefory(id);
    }
}
```

### 六、内容新增

**1.在 ego-service 中 TbContentDubboService 接口及实现类中添加方法**

```java
    /**
     * 新增内容
     * content
     */
    int insContent(TbContent content);

    @Override
    public int insContent(TbContent content) {
        return tbContentMapper.insertSelective(content);
    }
```

**2.在 ego-manage 的 TbContentService 接口及实现类中添加方法**

```java
    /**
     * 新增内容
     * content
     */
    int save(TbContent content); 

    @Override
    public int save(TbContent content) {
        Date date = new Date();
        content.setCreated(date);
        content.setUpdated(date);
        return tbContentDubboServiceImpl.insContent(content);
    }
```

**3.在 ego-manage 的 TbContentController 控制器中添加方法**

```java
    /**
     * 内容新增
     * content
     */
    @RequestMapping("content/save")
    @ResponseBody
    public EgoResult save(TbContent content) {
        EgoResult result = new EgoResult();
        int index = tbContentServiceImpl.save(content);
        if (index > 0) {
            result.setStatus(200);
        }
        return result;
    }
```

## 第六天 添加前后台大广告位数据缓存

### 一、分析门户中大广告需求

**1.在控制器中通过request作用域按照固定的json合适把数据传递给jsp**

**2.缓存运行图**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191127123542529.png)

### 二、实现分户中大广告显示（添加缓存）

**1.在 ego-service 的 TbContentService 及实现类中添加方法**

```java
    /**
     * 查询出最近的前n个内容
     * count,isSort
     * */
    List<TbContent> selByCount(int count,boolean isSort);

	@Override
    public List<TbContent> selByCount(int count, boolean isSort) {
        TbContentExample example = new TbContentExample();
        if (isSort) {
            //排序
            example.setOrderByClause("updated desc");
        }
        if (count != 0) {
            PageHelper.startPage(1, count);
            List<TbContent> list = tbContentMapper.selectByExampleWithBLOBs(example);
            PageInfo<TbContent> info = new PageInfo<>(list);
            return info.getList();
        } else {
            return tbContentMapper.selectByExampleWithBLOBs(example);
        }
    }
```

**2.新建ego-redis项目**

**3.在ego-redis的pom.xml中添加依赖**

```xml
<dependencies>
    <!-- jedis -->
    <dependency>
        <groupId>redis.clients</groupId>
        <artifactId>jedis</artifactId>
    </dependency>
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-webmvc</artifactId>
    </dependency>
</dependencies>
```

**4.在ego-redis中添加applicationContext-jedis.xml**

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans
        http://www.springframework.org/schema/beans/spring-beans-4.0.xsd">
    <!-- 配置jedis连接池 -->
    <bean id="jedisPoolConfig" class="redis.clients.jedis.JedisPoolConfig">
        <!-- 最大连接数 -->
        <property name="maxTotal" value="30"/>
        <!-- 最大空闲连接数 -->
        <property name="maxIdle" value="10"/>
        <!-- 每次释放连接的最大数目 -->
        <property name="numTestsPerEvictionRun" value="1024"/>
        <!-- 释放连接的扫描间隔（毫秒） -->
        <property name="timeBetweenEvictionRunsMillis" value="30000"/>
        <!-- 连接最小空闲时间 -->
        <property name="minEvictableIdleTimeMillis" value="1800000"/>
        <!-- 连接空闲多久后释放, 当空闲时间>该值 且 空闲连接>最大空闲连接数 时直接释放 -->
        <property name="softMinEvictableIdleTimeMillis" value="10000"/>
        <!-- 获取连接时的最大等待毫秒数,小于零:阻塞不确定的时间,默认-1 -->
        <property name="maxWaitMillis" value="1500"/>
        <!-- 在获取连接的时候检查有效性, 默认false -->
        <property name="testOnBorrow" value="true"/>
        <!-- 在空闲时检查有效性, 默认false -->
        <property name="testWhileIdle" value="true"/>
        <!-- 连接耗尽时是否阻塞, false报异常,ture阻塞直到超时, 默认true -->
        <property name="blockWhenExhausted" value="false"/>
    </bean>
    <!-- jedisCluster -->
    <bean id="jedisClients" class="redis.clients.jedis.JedisCluster">
        <constructor-arg name="poolConfig" ref="jedisPoolConfig"/>
        <constructor-arg name="nodes">
            <set>
                <bean class="redis.clients.jedis.HostAndPort">
                    <constructor-arg name="host" value="192.168.2.130"/>
                    <constructor-arg name="port" value="7001"/>
                </bean>
                <bean class="redis.clients.jedis.HostAndPort">
                    <constructor-arg name="host" value="192.168.2.130"/>
                    <constructor-arg name="port" value="7002"/>
                </bean>
                <bean class="redis.clients.jedis.HostAndPort">
                    <constructor-arg name="host" value="192.168.2.130"/>
                    <constructor-arg name="port" value="7003"/>
                </bean>
                <bean class="redis.clients.jedis.HostAndPort">
                    <constructor-arg name="host" value="192.168.2.130"/>
                    <constructor-arg name="port" value="7004"/>
                </bean>
                <bean class="redis.clients.jedis.HostAndPort">
                    <constructor-arg name="host" value="192.168.2.130"/>
                    <constructor-arg name="port" value="7005"/>
                </bean>
                <bean class="redis.clients.jedis.HostAndPort">
                    <constructor-arg name="host" value="192.168.2.130"/>
                    <constructor-arg name="port" value="7006"/>
                </bean>
            </set>
        </constructor-arg>
    </bean>
</beans>
```

**5.在ego-redis中添加redis.properties软编码**

```properties
redis.bigPic.key= bigPic
```

**6.在ego-redis中新建JedisDao接口及实现类**

```java
public interface JedisDao {
    /**
     * 判断key是否存在
     */
    boolean exists(String key);

    /**
     * 删除key
     */
    Long del(String key);

    /**
     * 设置或覆盖key
     */
    String set(String key, String value);

    /**
     * 获取key
     */
    String get(String key);
}

@Repository
public class JedisDaoImpl implements JedisDao {
    @Resource
    private JedisCluster jedisCluster;

    @Override
    public boolean exists(String key) {
        return jedisCluster.exists(key);
    }

    @Override
    public Long del(String key) {
        return jedisCluster.del(key);
    }

    @Override
    public String set(String key, String value) {
        return jedisCluster.set(key,value);
    }

    @Override
    public String get(String key) {
        return jedisCluster.get(key);
    }
}
```

**7.在ego-commos中新建JsonUtils工具类**

```java
/**
 * 易购商城自定义响应结构
 */
public class JsonUtils {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    /**
     * 将对象转换成json字符串。
     * <p>Title: pojoToJson</p>
     * <p>Description: </p>
     * @param data
     * @return
     */
    public static String objectToJson(Object data) {
    	try {
			String string = MAPPER.writeValueAsString(data);
			return string;
		} catch (JsonProcessingException e) {
			e.printStackTrace();
		}
    	return null;
    }
    
    /**
     * 将json结果集转化为对象
     * 
     * @param jsonData json数据
     * @param class 对象中的object类型
     * @return
     */
    public static <T> T jsonToPojo(String jsonData, Class<T> beanType) {
        try {
            T t = MAPPER.readValue(jsonData, beanType);
            return t;
        } catch (Exception e) {
        	e.printStackTrace();
        }
        return null;
    }
    
    /**
     * 将json数据转换成pojo对象list
     * <p>Title: jsonToList</p>
     * <p>Description: </p>
     * @param jsonData
     * @param beanType
     * @return
     */
    public static <T>List<T> jsonToList(String jsonData, Class<T> beanType) {
    	JavaType javaType = MAPPER.getTypeFactory().constructParametricType(List.class, beanType);
    	try {
    		List<T> list = MAPPER.readValue(jsonData, javaType);
    		return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
    	return null;
    }
}
```

**8.在ego-portal中新建TbContentService接口及实现类**

```java
public interface TbContentService {
    /**
     * 显示大广告
     */
    String showBigPic();
}

@Service
public class TbContentServiceImpl implements TbContentService {
    @Reference
    private TbContentDubboService tbContentDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${redis.bigPic.key}")
    private String key;

    @Override
    public String showBigPic() {
        if (jedisDaoImpl.exists(key)) {
            String value = jedisDaoImpl.get(key);
            if (value != null && !value.equals("")) {
                return value;
            }
        }
        List<TbContent> list = tbContentDubboServiceImpl.selByCount(6, true);
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (TbContent content : list) {
            Map<String, Object> map = new HashMap<>();
            map.put("srcB", content.getPic2());
            map.put("height", 240);
            map.put("alt", "图片未加载");
            map.put("width", 670);
            map.put("src", content.getPic());
            map.put("widthB", 500);
            map.put("href", content.getUrl());
            map.put("heightB", 240);
            mapList.add(map);
        }
        String listJson = JsonUtils.objectToJson(mapList);
        //把数据存储到redis中
        jedisDaoImpl.set(key, listJson);
        return listJson;
    }
}
```

**9.在ego-portal中新建TbContentController控制器**

```java
@Controller
public class TbContentController {
    @Resource
    private TbContentService tbContentServiceImpl;

    @RequestMapping("showBigPic")
    public String showBigPic(Model model){
        model.addAttribute("ad1",tbContentServiceImpl.showBigPic());
        return "index";
    }
}
```

**11.在ego-portal的PageController中修改方法**

```java
@RequestMapping("/")
public String welcome() {
    return "forward:/showBigPic";
}
```

## 第七天 SolrJ操作SolrCloud

### 一、Solr简介

**1.solr是什么**

1.1是一个war项目

**2.自己的项目怎么和Solr进行交互**

2.1特定的API叫做SolrJ

**3.具备数据持久化功能**

3.1Solr中会存储需要进行搜索的数据

3.2把所有数据都初始化到Solr中

**4.Solr作用（什么时候使用Solr）**

4.1大量数据检索时，使用Solr能提升检锁效率

**5.Solr是基于索引进行查询的**

5.1顺序查询：从内容的最开始找到内容为止

5.2反向检索引：

5.2.1把内容进行拆分

**6.国内实现检索的常用方案**

6.1apache lucene：实现检索的解决方案（Solr基于lucene）

6.2Baidu API

6.3Google API

### 二、IKAnalyzer中文拆词器

**1.Solr默认对中文拆词功能支持不好**

1.1解决方案：使用IK Analyzer

**2.配置IK Analyzer时，实际上是给Solr新建了一个filedType，只要某个属性是这个类型，这个属性就会使用IK Analyzer进行拆词**

### 三、Solr管理界面的docments菜单功能

**1.包含了solr数据的新增，删除，修改三个功能**

**2.数据支持很多种格式：json或xml等**

**3.新增时，必须包含对id的新增**

**4.每次新增，solr会新建一个SolrDocument对象，这个对象存储新增的内容**

4.1把SolrDocument理解成实体类对象

4.2包含了Solr的schema.xml中配置的所有filed

4.3新增时有什么属性，最终显示时就只有什么属性

**5.把DocumentType以xml方式举例**

5.1新增

```xml
<doc>
	<field name="id">change.me</field>
	<field name="title">change.me</field>
</doc>
```

5.2修改：只要id已经存在，就执行修改 

5.3删除

5.3.1根据id删除

```xml
<delete>
	<id>change.me</id>
</delete>
<commit/>
```

5.3.2删除全部

```xml
<delete>
	<query>*:*</query>
</delete>
<commit/>
```

### 四、管理界面query菜单功能

**1.q表示查询条件**

1.1* : *查询全部

1.2chinese_tx:java培训

1.2.1把java培训拆分成java和培训，按照java和培训在chinese_tx属性中进行查找

1.3chinses_tx:"java培训"，java培训不会被拆词

1.4在schema.xml中配置符合属性，这个符合属性可以包含多个其他属性的值

1.4.1在schema.xml添加以下内容

```xml
<field name="chinese" type="text_ik" indexed="true" stored="true" multiValued="true"/>
<copyField source="chinese_tx" dest="chinese"/>
<copyField source="chinese_text" dest="chinese"/>
<field name="chinese_text" type="text_ik" indexed="true" stored="true"/>
<field name="chinese_tx" type="text_ik" indexed="true" stored="true"/>
<fieldType name="text_ik" class="solr.TextField">
    <analyzer class="org.wltea.analyzer.lucene.IKAnalyzer"/>
</fieldType>
```

### 五、SolrCloud

**1.solr是web项目，需要放入到romcat中，依赖多个tomcat，让多个tomcat之间能够通信**

**2.需要借助zookeeper实现tomcat之间通信**

**3.结构图**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191130185024384.png)

## 第八天 完成商品搜索功能

### 一、商品搜索功能业务分析

**1.搜索功能写在 ego-search 项目中**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191130202929677.png)

**2.在编写 ego-search 项目的控制器时，需要发送给 jsp 多个值**

2.1${query} 搜索内容

2.2${itemList} 搜索到商品列表

2.2.1商品图片是数组类型

2.3${totalPages} 总页数

2.4${page} 当前页

**3.Solr 完成步骤：**

3.1先配置field

3.1.1注意：field的名称不要和原有名称重复

3.2数据初始化

3.3完成搜索功能

### 二、配置 Solr 的 Field

**1.已经在 Solr中安装好IK Analyzer**

**2.在Solrhome/collection1/conf/schema.xml中配置业务字段**

```xml
<field name="item_title" type="text_ik" indexed="true" stored="true"/>
<field name="item_sell_point" type="text_ik" indexed="true" stored="true"/>
<field name="item_price"  type="long" indexed="true" stored="true"/>
<field name="item_image" type="string" indexed="false" stored="true" />
<field name="item_category_name" type="string" indexed="true" stored="true" />
<field name="item_desc" type="text_ik" indexed="true" stored="false" />

<field name="item_keywords" type="text_ik" indexed="true" stored="false" multiValued="true"/>
<copyField source="item_title" dest="item_keywords"/>
<copyField source="item_sell_point" dest="item_keywords"/>
<copyField source="item_category_name" dest="item_keywords"/>
<copyField source="item_desc" dest="item_keywords"/>
```

### 三、创建ego-search项目

**1.新建ego-search项目**

**2.在ego-parent中添加依赖**

```xml
<!--solr-->
<dependency>
    <groupId>org.apache.solr</groupId>
    <artifactId>solr-solrj</artifactId>
    <version>5.3.1</version>
</dependency>
```

**3.把ego-manage的pom.xml粘贴到ego-search**

3.1修改端口为8083

3.2添加solr的依赖

3.3删除ego-redis的依赖

3.3.1如果不删除，且redis服务器没有启动，会在启动tomcat时，停在init WebapplicationContext

**4.把ego-manage的webapps下的web.xml复制到ego-search中的webapps里面导入页面**

**5.把ego-manage的配置文件复制到ego-search中**

5.1applicationContext-dubbo.xml修改应用程序名和注解扫描包

### 四、Solr数据初始化

**1.由于ego-manage没有提供solr初始化按钮等功能**

1.1只能在ego-search编写控制器，通过浏览器访问实现数据初始化

**2.在dubbo中consumer调用provider默认最大传输量为8MB**

2.1<dubbo:protocal />中payload=“8388608”，取值为int，表示每次consumer向provider请求数据时数据相应最大量单位为字节

**3.实现步骤**

3.1在ego-service的TbItemDubboService及实现类中添加方法

```java
	/**
     * 通过状态查询全部可用数据
     * */
    List<TbItem> selAllByStatus(byte status);

 	@Override
    public List<TbItem> selAllByStatus(byte status) {
        TbItemExample example = new TbItemExample();
        example.createCriteria().andStatusEqualTo(status);
        return tbItemMapper.selectByExample(example);
    }
```

3.2在ego-service的TbItemDescDubboSerice及实现类中添加方法

```java
    /**
     * 根据主键查询商品描述对象
     * */
    TbItemDesc selByItemId(long itemId);

    @Override
    public TbItemDesc selByItemId(long itemId) {
        return tbItemDescMapper.selectByPrimaryKey(itemId);
    }
```

3.3在ego-search中新建applicationContext-solr.xml配置文件

```xml
    <bean id="solrClient" class="org.apache.solr.client.solrj.impl.CloudSolrClient">
        <constructor-arg type="java.lang.String" value="192.168.88.131:2181,192.168.88.131:2182,192.168.88.131:2183"/>
        <property name="defaultCollection" value="collection1"/>
    </bean>
```

3.4在ego-search中新建TbItemService及实现类

```java
public interface TbItemService {
    void init() throws IOException, SolrServerException;
}

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    @Reference
    private TbItemCatDubboService tbItemCatDubboServiceImpl;
    @Reference
    private TbItemDescDubboService tbItemDescDubboServiceImpl;
    @Resource
    private CloudSolrClient solrClient;

    @Override
    public void init() throws IOException, SolrServerException {
        //查询所有在售的商品
        List<TbItem> items = tbItemDubboServiceImpl.selAllByStatus((byte) 1);
        for (TbItem item : items) {
            //查询商品对应的类目信息
            TbItemCat cat = tbItemCatDubboServiceImpl.selById(item.getCid());
            //查询商品对应的描述信息
            TbItemDesc desc = tbItemDescDubboServiceImpl.selByItemId(item.getId());
            SolrInputDocument document=new SolrInputDocument();
            document.addField("id",item.getId());
            document.addField("item_title",item.getTitle());
            document.addField("item_sell_point",item.getSellPoint());
            document.addField("item_price",item.getPrice());
            document.addField("item_image",item.getImage());
            document.addField("item_category_name",cat.getName());
            document.addField("item_desc",desc.getItemDesc());
            solrClient.add(document);
        }
        solrClient.commit();
    }
}
```

3.5在ego-search中新建TbItemController控制器

```java
@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceImpl;

    @RequestMapping(value = "solr/init",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String init() {
        long start = System.currentTimeMillis();
        try {
            tbItemServiceImpl.init();
            long end = System.currentTimeMillis();
            return "初始化总时间" + (end - start) / 1000 + "秒";
        } catch (Exception e) {
            e.printStackTrace();
            return "初始化失败！";
        }
    }
}
```

### 五、门户搜索功能

**1.在ego-service中新建TbItemChild实体类**

```java
public class TbItemChild extends TbItem {
    private String[] images;

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }
}
```

**2.在ego-serach的TbItemService及实现类中添加方法**

```java
    /**
     * 分页查询
     */
    Map<String, Object> selByQuery(String query, int page, int rows) throws IOException, SolrServerException;

    @Override
    public Map<String, Object> selByQuery(String query, int page, int rows) throws IOException, SolrServerException {
        SolrQuery params = new SolrQuery();
        //设置分页条件
        params.setStart(rows * (page - 1));
        params.setRows(rows);
        //设置条件
        params.setQuery("item_keywords:" + query);
        //设置高亮
        params.setHighlight(true);
        params.addHighlightField("item_title");
        params.setHighlightSimplePre("<span style='color:red'/>");
        params.setHighlightSimplePost("</span");
        QueryResponse response = solrClient.query(params);
        List<TbItemChild> listChild = new ArrayList<>();
        //未高亮内容
        SolrDocumentList listSolr = response.getResults();
        Map<String, Map<String, List<String>>> map = response.getHighlighting();
        for (SolrDocument document : listSolr) {
            TbItemChild child = new TbItemChild();
            child.setId(Long.parseLong(document.getFieldValue("id").toString()));
            List<String> list = map.get(document.getFieldValue("id")).get("item_title");
            if (list != null && list.size() > 0) {
                child.setTitle(list.get(0));
            } else {
                child.setTitle(document.getFieldValue("item_title").toString());
            }
            child.setPrice((Long) document.getFieldValue("item_price"));
            Object item_images = document.getFieldValue("item_image");
            child.setImages(item_images == null || item_images.equals("") ? new String[1] : item_images.toString().split(","));
            child.setSellPoint(document.getFieldValue("item_sell_point").toString());
            listChild.add(child);
        }
        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("itemList", listChild);
        resultMap.put("totalPages", listSolr.getNumFound() % rows == 0 ? listSolr.getNumFound() / rows : listSolr.getNumFound() / rows + 1);
        return resultMap;
    }
```

**3.在ego-search的TbItemController控制器中添加方法**

```java
    /**
     * 搜索功能
     */
    @RequestMapping("search.html")
    public String search(Model model, String q, @RequestParam(defaultValue = "1") int page, @RequestParam(defaultValue = "12") int rows) {
        try {
            q = new String(q.getBytes("iso-8859-1"), "utf-8");
            Map<String, Object> map = tbItemServiceImpl.selByQuery(q, page, rows);
            model.addAttribute("query", q);
            model.addAttribute("itemList", map.get("itemList"));
            model.addAttribute("totalPages", map.get("totalPages"));
            model.addAttribute("page", page);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "search";
    }
```

### 六、ego-search新增功能

**1.在ego-search的TbItemService及实现类中添加方法**

```java
    /**
     * 新增商品到solr
     * */
    int add(Map<String,Object> map,String desc) throws IOException, SolrServerException;

    @Override
    public int add(Map<String,Object> map,String desc) throws IOException, SolrServerException {
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", map.get("id"));
        document.setField("item_title", map.get("title"));
        document.setField("item_sell_point",map.get("sellPoint"));
        document.setField("item_price", map.get("price"));
        document.setField("item_image", map.get("image"));
        document.setField("item_category_name", tbItemCatDubboServiceImpl.selById((Integer) map.get("cid")).getName());
        document.setField("item_desc", desc);
        UpdateResponse response = solrClient.add(document);
        solrClient.commit();
        if (response.getStatus() == 0) {
            return 1;
        }
        return 0;
    }
```

**2.在ego-search的TbItemController控制器中添加方法**

2.1@RequestBody

```java
    /**
     * 新增，返回值为1成功
     */
    @RequestMapping("solr/add")
    @ResponseBody
    public int add(@RequestBody Map<String, Object> map) {
        System.out.println(map);
        try {
            return tbItemServiceImpl.add((LinkedHashMap) map.get("item"), map.get("desc").toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
```

### 七、HttpClient简介

**1.由apache推出的实现使用java代码完成请求/响应的一套API**

**2.实现效果**

2.1模拟浏览器发送请求及解析响应内容

**3.常用类**

3.1CloseableHttpClient：负责发送请求和接收响应，相当于浏览器

3.2HttpPost：请求对象，所有请求信息都放入到这个对象中

3.2.1HttpGet：get方式的请求

3.3CloseableHttpResponse：响应对象，所有响应信息放入到这个类

3.4EntityUtils：工具类，解析响应体

### 八、实现后台商品新增时同步Solr数据

**1.在ego-parent的pom.xml文件中引入httpclient的依赖**

```xml
<!--httpclient-->
<dependency>
    <groupId>org.apache.httpcomponents</groupId>
    <artifactId>httpclient</artifactId>
    <version>4.4.1</version>
</dependency>
```

**2.在ego-commons中添加httpclient依赖和HttpClinetUtil工具类并在commons.properties中添加solr的url**

```properties
search.url = http://localhost:8083/solr/add
```

**3.在ego-manage的TbItemService及实现类中添加语句**

3.1doPostJson()设置请求头中内容类型为json把参数以流的形式传递给服务器

```java
        final TbItem itemFinal = item;
        final String descFinal = desc;
		new Thread(){
            @Override
            public void run() {
                Map<String, Object> map = new HashMap<>();
                map.put("item", itemFinal);
                map.put("desc", descFinal);
                HttpClientUtil.doPostJson(url, JsonUtils.objectToJson(map));
            }
        }.start();
```

## 第九天 显示商品详细详情

### 一、商品详情需求分析

**1.在ego-item的item.jsp中完成3个功能**

1.1显示商品信息

1.2显示商品的描述

1.3显示商品的规格参数

**2.商品详细信息通过作用域传递到页面{item.xxx}**

2.1商品对象包含了5个值

**3.显示商品描述功能**

3.1使用js的setTimeout延迟1秒加载，提升商品详细信息的用户体验

3.2以后项目中如果页面数据量过大，可以考虑一些不是立即看到的内容选择延迟加载

**4.商品规格参数通过点击tab发送请求**

### 二、显示商品详情

**1.把ego-search中的TbItemChild实体类剪切到ego-commons中**

**2.在ego-service的TbItemDubboService及是西安类中添加方法**

```java
    /**
     * 根据主键查询商品信息
     */
    TbItem selById(long id);

    @Override
    public TbItem selById(long id) {
        return tbItemMapper.selectByPrimaryKey(id);
    }
```

**3.在ego-item中新建TbItemService及实现类**

```java
public interface TbItemService {
    /**
     * 显示商品详情
     */
    TbItemChild show(long id);
}

@Service
public class TbItemServiceImpl implements TbItemService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;

    @Override
    public TbItemChild show(long id) {
        TbItem item = tbItemDubboServiceImpl.selById(id);
        TbItemChild itemChild = new TbItemChild();
        itemChild.setId(item.getId());
        itemChild.setTitle(item.getTitle());
        itemChild.setPrice(item.getPrice());
        itemChild.setSellPoint(item.getSellPoint());
        System.out.println(item.getImage());
        itemChild.setImages(item.getImage() != null && !item.getImage().equals("") ? item.getImage().split(",") : new String[1]);
        return itemChild;
    }
}
```

**4.在ego-item中新建TbItemController控制器**

```java
@Controller
public class TbItemController {
    @Resource
    private TbItemService tbItemServiceImpl;

    /**
     * 显示商品详情
     */
    @RequestMapping("item/{id}.html")
    public String showItemDetails(@PathVariable long id, Model model) {
        model.addAttribute("item", tbItemServiceImpl.show(id));
        return "item";
    }
}
```

### 三、商品详情缓存添加

**1.在ego-redis的redis.properties中添加key**

```properties
redis.item.key = item
```

**2.在ego-parent的pom.xml添加对ego-redis的依赖**

```xml
<!--redis依赖-->
<dependency>
    <groupId>com.ego</groupId>
    <artifactId>ego-redis</artifactId>
    <version>${myredis-version}</version>
</dependency>
```

**3.在ego-item的applicationContext-spring.xml中修改属性加载文件**

```xml
<context:property-placeholder location="classpath*:*.properties"/>
```

**4.在ego-item的pom.xml中添加ego-redis的依赖**

```xml
<!--redis依赖-->
<dependency>
    <groupId>com.ego</groupId>
    <artifactId>ego-redis</artifactId>
</dependency>
```

**5.在ego-item的TbItemServiceImpl中添加代码**

```java
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${redis.item.key}")
    private String itemKey;

    @Override
    public TbItemChild show(long id) {
        String key = itemKey + id;
        if (jedisDaoImpl.exists(key)) {
            String json = jedisDaoImpl.get(key);
            if (json != null && !json.equals("")) {
                return JsonUtils.jsonToPojo(json, TbItemChild.class);
            }
        }
        TbItem item = tbItemDubboServiceImpl.selById(id);
        TbItemChild itemChild = new TbItemChild();
        itemChild.setId(item.getId());
        itemChild.setTitle(item.getTitle());
        itemChild.setPrice(item.getPrice());
        itemChild.setSellPoint(item.getSellPoint());
        System.out.println(item.getImage());
        itemChild.setImages(item.getImage() != null && !item.getImage().equals("") ? item.getImage().split(",") : new String[1]);
        //存到redis数据库中
        jedisDaoImpl.set(key, JsonUtils.objectToJson(itemChild));
        return itemChild;
    }
```

### 四、后台项目商品同步redis

**1.商品上架不需要操作redis，下架和删除都是从redis中删除key**

**2.在ego-manage的TbItemServiceImpl的update（）方法中添加代码**

```java
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${redis.item.key}")
    private String itemKey;	

	@Override
    public int update(String ids, byte status) {
        TbItem item = new TbItem();
        String[] idsStr = ids.split(",");
        int index = 0;
        for (String id : idsStr) {
            item.setId(Long.parseLong(id));
            item.setStatus(status);
            index += tbItemDubboServiceimpl.updItemStatus(item);
            if (status==2||status==3){
                System.out.println(itemKey+id);
                jedisDaoImpl.del(itemKey+id);
            }
        }
        if (index == idsStr.length) {
            return 1;
        } else {
            return 0;
        }
    }
```

### 五、商品描述

**1.在ego-redis的redis.properties中添加key**

```properties
redis.desc.key = desc
```

**2.在ego-item中新建TbItemService及实现类**

```java
public interface TbItemDescService {
    /**
     * 根据商品id显示商品藐视
     */
    String showDesc(long itemId);
}

@Service
public class TbItemDescServiceImpl implements TbItemDescService {
    @Reference
    private TbItemDescDubboService tbItemDescDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${redis.desc.key}")
    private String descKey;

    @Override
    public String showDesc(long itemId) {
        String key = descKey + itemId;
        if (jedisDaoImpl.exists(key)) {
            String json = jedisDaoImpl.get(key);
            if (json != null && !json.equals("")) {
                return json;
            }
        }
        String result = tbItemDescDubboServiceImpl.selByItemId(itemId).getItemDesc();
        jedisDaoImpl.set(key, result);
        return result;
    }
}
```

**3.在ego-item中新建TbItemDescController控制器**

```java
@Controller
public class TbItemDescController {
    @Resource
    private TbItemDescService tbItemDescServiceImpl;

    /**
     * 显示商品详情
     */
    @RequestMapping(value = "item/desc/{id}.html",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String desc(@PathVariable long id) {
        return tbItemDescServiceImpl.showDesc(id);
    }
}
```

### 六、为了测试方便后台采用更新时间降序

**1.在solrhome/collection1/conf/schema.xml中添加yigeField**

```xml
<field name="item_updated" type="date" indexed="true" stored="false"/> 
```

**2.在ego-search的init方法中添加**

```java
document.setField("item_updated",desc.getUpdated());
```

**3.在ego-search的selByQuery中添加**

```java
//添加排序
params.setSort("item_updated", SolrQuery.ORDER.desc);
```

### 七、实现商品规格参数

**1.在ego-service中新建TbItemParamItemDubboService及实现类**

```java
public interface TbItemParamItemDubboService {
    /**
     * 根据商品id查询商品规格参数
     */
    TbItemParamItem selByItemId(long itemId);
}

public class TbItemParamItemDubboServiceImpl implements TbItemParamItemDubboService {
    @Resource
    private TbItemParamItemMapper tbItemParamItemMapper;

    @Override
    public TbItemParamItem selByItemId(long itemId) {
        TbItemParamItemExample example=new TbItemParamItemExample();
        example.createCriteria().andItemIdEqualTo(itemId);
        List<TbItemParamItem> list = tbItemParamItemMapper.selectByExampleWithBLOBs(example);
        if (list!=null&&list.size()>0)
            return list.get(0);
        return null;
    }
}
```

**2.在ego-service-impl的applicationContext-dubbo.xml中注册服务**

```xml
<!--商品规格分类-->
    <dubbo:service interface="com.ego.dubbo.service.TbItemParamItemDubboService"
                   ref="tbItemParamItemDubboService"/>
    <bean id="tbItemParamItemDubboService" class="com.ego.dubbo.service.impl.TbItemParamItemDubboServiceImpl"/>
```

**3.在ego-item中新建ParamItem，ParamNode实体类**

```java
public class ParamItem {
    private String group;
    private List<ParamNode> params;

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<ParamNode> getParams() {
        return params;
    }

    public void setParams(List<ParamNode> params) {
        this.params = params;
    }
}

public class ParamNode {
    private String k;
    private String v;
    
    public String getK() {
        return k;
    }

    public void setK(String k) {
        this.k = k;
    }

    public String getV() {
        return v;
    }

    public void setV(String v) {
        this.v = v;
    }
}
```

**4.在ego-item中新建TbItemParamItemService及实现类**

```java
public interface TbItemParamItemService {
    /**
     * 显示商品规格参数
     */
    String showParam(long itemId);
}

@Service
public class TbItemParamItemServiceImpl implements TbItemParamItemService {
    @Reference
    private TbItemParamItemDubboService tbItemParamItemDubboServiceImpl;

    @Override
    public String showParam(long itemId) {
        TbItemParamItem item = tbItemParamItemDubboServiceImpl.selByItemId(itemId);
        List<ParamItem> list = JsonUtils.jsonToList(item.getParamData(), ParamItem.class);
        System.out.println(list);
        StringBuffer sf = new StringBuffer();
        for (ParamItem paramItem : list) {
            sf.append("<table width='500'>");
            sf.append("<tr>");
            for (int i = 0; i < paramItem.getParams().size(); i++) {
                if (i == 0) {
                    sf.append("<tr>");
                    sf.append("<td align='right' width='30%'>" + paramItem.getGroup() + "</td>");
                    sf.append("<td align='right' width='30%'>" + paramItem.getParams().get(i).getK() + "</td>");
                    sf.append("<td align='left' width='30%'>" + paramItem.getParams().get(i).getV() + "</td>");
                    sf.append("</tr>");
                }else {
                    sf.append("<tr>");
                    sf.append("<td> </td>");
                    sf.append("<td align='right' width='30%'>" + paramItem.getParams().get(i).getK() + "</td>");
                    sf.append("<td align='left' width='30%'>" + paramItem.getParams().get(i).getV() + "</td>");
                    sf.append("</tr>");
                }
            }
            sf.append("</tr>");
            sf.append("</table");
            sf.append("<hr/>");
        }
        return sf.toString();
    }
}
```

**5.在ego-item中新建TbItemParamItemController控制器**

```java
@Controller
public class TbItemParamItemController {
    @Resource
    private TbItemParamItemService tbItemParamItemServiceImpl;

    @RequestMapping(value = "item/param/{id}.html",produces = "text/html;charset=utf-8")
    @ResponseBody
    public String param(@PathVariable long id){
        return tbItemParamItemServiceImpl.showParam(id);
    }
}
```

## 第十天 完成SSO单点登录

### 一、SSO简介

**1.SSO英文名称SingleSignOn**

**2.SSO中文名称：单点登录**

**3.单点登录解释**

3.1一次登录，让其他项目能够共享登陆状态

3.2本质：使用特定技术在分布式项目中模拟HsspSession功能

3.2.1技术选型：Redis+Cookie模拟HttpSession功能

**4.传统项目和分布式项目登录功能对比**

4.1传统项目

<img src="C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191215195242740.png"  />

4.2分布式项目

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191215200322789.png)

### 二、Cookie

**1.客户端存值技术**

1.1存储位置客户端浏览器

1.2作用：存值

1.3存值类型：只能存储字符串

**2.Cookie运行原理**

2.1当浏览器输入URL访问服务器时会自动携带所有有效Cookie（时间内，指定路径内，指定域名内），tomcat接收请求后把Cookie放入到HttpServletRequest中，在代码中通过request对象获取Cookie的内容

2.2服务器内容可以产生Cookie内容，需要放入到响应对象中响应给客户端浏览器，（要求跳转类型为重定向），客户端浏览器接收响应内容后把Cookie内容存储到指定文件夹内容

**3.产生Cookie**

```java
@RequestMapping("demo")
public String goIndex(HttpServletRespongse response){
    Cookie c = new Cookie("key","value");
    //1.默认时间和HttpSession相同
    //设置cookie存活时间，单位秒
    //c.setMaxAge(10);
    //2.默认存储路径为path=/
    //设置那个目录下资源能访问
    c.setPath("/");
    //3.域名和当前项目的与名相同
    setDemain(".ego.com");
    response.addCookie(c);
    retrun "redirect:/index.jsp";
}
```

**4.获取Cookie**

```jsp
<%
Cookie [] cs=request.getCookies();
if(cd!=null){
    for(Cookie c:cs){
        out.print("key:"+c.getName()+",value:"+c.getValue()+"<br/>");
    }
}else{
    out.print("没有cookie")
}
%>
```

### 三、HttpSession运行原理

1.当客户端浏览器访问服务器时，服务器接收请求后会判断请求中是否在Cookie包含JSESSIONID

2.如果包含JSESSIONID把对应值取出，作为key从全局Map<String,Session>对象，往出取session对象

3.如果有对应key，把session对象取出，根据自己的业务员添加操作

4.如果请求对象中JSESSIONID在全局Map<String,Session>没有或请求对象中Cookie没有JSESSIONID，会执行新建session步骤

5.Tomcat会新建（new）一个Session对象，同时产生一个UUID，把UUID作为Map的key，新建的Session对象作为value，还会把UUID放入到Cookie作为value，value对应的key是JSESSIONID

### 四、使用Redis+Cookie模拟Session步骤

**1.流程图**

![](C:\Users\simptx\AppData\Roaming\Typora\typora-user-images\image-20191215215746401.png)

**2.步骤解释**

2.1步骤1：第一次请求时Cookie中没有token

2.2产生一个UUID，把”token“作为Cookie的key，UUID作为Cookie的value

2.3如果希望类似往Session存储内容，直接把UUID当作redis的key（个别需求该需要考虑key重复问题），把需要存储的内容作为value

2.4把cookie对象相应给客户端浏览器

### 五、登录业务

**1.登录时，跳转到ego-sign的login.jsp**

**2.从哪个跳转到登录页面，登录成功后跳转回哪里**

2.1借助请求头中的Referer，获取到从哪里跳转过来

**3.实现SSO模拟Session功能**

**4.当用户点击“加入购物车”按钮**

4.1如果已经登录，进入购物车页面

4.2如果没有登录，进入登录页面

### 六、实现登录功能

**1.在ego-service中新建TbUserDubboService及实现类**

```java
public interface TbUserDubboService {
    /**
     * 根据用户和密码查询登录
     */
    TbUser selByUser(TbUser user);

}

public class TbUserDubboServiceImpl implements TbUserDubboService {
    @Resource
    private TbUserMapper tbUserMapper;

    @Override
    public TbUser selByUser(TbUser user) {
        TbUserExample example = new TbUserExample();
 		 example.createCriteria().andUsernameEqualTo(user.getUsername()).andPasswordEqualTo(user.getPassword());
        List<TbUser> list = tbUserMapper.selectByExample(example);
        if (list!=null&&list.size()>0){
            return list.get(0);
        }
        return null;
    }
}
```

**2.在ego-service-impl的applicationContext-dubbo.xml配置文件中注册服务**

```xml
<!--用户-->
<dubbo:service interface="com.ego.dubbo.service.TbUserDubboService"
                   ref="tbUserDubboService"/>
<bean id="tbUserDubboService" class="com.ego.dubbo.service.impl.TbUserDubboServiceImpl"/>
```

**3.在ego-commons的EgoResult实体类中添加String msg属性，并获取getter，setter方法**

**4.在ego-sign中新建TbUserService及实现类**

```java
public interface TbUserService {
    /**
     * 登录
     */
    EgoResult login(TbUser user);
}

@Service
public class TbUserServiceImpl implements TbUserService {
    @Reference
    private TbUserDubboService tbUserDubboServiceImpl;

    @Override
    public EgoResult login(TbUser user) {
        EgoResult result = new EgoResult();
        TbUser userSelect = tbUserDubboServiceImpl.selByUser(user);
        if (userSelect != null) {
            result.setStatus(200);
        } else {
            result.setMsg("用户名或密码错误");
        }
        return result;
    }
}
```

**5.在ego-sign中新建TbUserController控制器添加方法**

```java
@Controller
public class TbUserController {
    @Resource
    private TbUserService tbUserServiceImpl;

    /**
     * 显示登陆
     */
    @RequestMapping("user/showLogin")
    public String showLogin(@RequestHeader("Referer") String url, Model model) {
        model.addAttribute("redirect", url);
        return "login";
    }

    /**
     * 登录
     */
    @RequestMapping("user/login")
    @ResponseBody
    public EgoResult login(TbUser user) {
        return tbUserServiceImpl.login(user);
    }
}
```

### 七、实现SSO核心功能

**1.在ego-commons中放入工具类CookieUtils**

**2.在ego-sign的TbUserService及实现类中修改login方法**

```java
     /**
     * 登录
     */
    EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response);


	@Reference
    private TbUserDubboService tbUserDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;

    @Override
    public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
        EgoResult result = new EgoResult();
        TbUser userSelect = tbUserDubboServiceImpl.selByUser(user);
        if (userSelect != null) {
            result.setStatus(200);
            //当用户登录成功后把用户信息放入到redis中
            String key = UUID.randomUUID().toString();
            jedisDaoImpl.set(key, JsonUtils.objectToJson(userSelect));
            //设置key的有效时间
            jedisDaoImpl.expire(key, 60 * 60 * 24 * 3);
            //产生cookie
            CookieUtils.setCookie(request, response, "TT_TOKEN", key, 60 * 60 * 24 * 3);
        } else {
            result.setMsg("用户名或密码错误");
        }
        return result;
    }
```

**3.在ego-sign的TbUserController控制器中修改方法**

```java
    /**
     * 登录
     */
    @RequestMapping("user/login")
    @ResponseBody
    public EgoResult login(TbUser user, HttpServletRequest request, HttpServletResponse response) {
        return tbUserServiceImpl.login(user, request, response);
    }
```

### 八、实现通过token获取用户信息

**1.在ego-sign的TbUserService及实现类中添加方法**

```java
    /**
     * 根据token查询用户信息
     */
    EgoResult getUserInfoByToken(String token);

    @Override
    public EgoResult getUserInfoByToken(String token) {
        EgoResult result = new EgoResult();
        String json = jedisDaoImpl.get(token);
        if (json != null && !json.equals("")) {
            TbUser user = JsonUtils.jsonToPojo(json, TbUser.class);
            //把密码清空
            user.setPassword(null);
            result.setStatus(200);
            result.setMsg("OK");
            result.setData(user);
        }else{
            result.setMsg("获取失败");
        }
        return result;
    }
```

**2.在ego-sign的TbUserController控制器中添加方法**

```java
    /**
     * 通过token获取用户信息
     */
    @RequestMapping("user/token/{token}")
    @ResponseBody
    public Object getUserInfo(@PathVariable String token, String callback) {
        EgoResult result = tbUserServiceImpl.getUserInfoByToken(token);
        if (callback != null && !callback.equals("")) {
            MappingJacksonValue mjv = new MappingJacksonValue(result);
            mjv.setJsonpFunction(callback);
            return mjv;
        }
        return result;
    }
```

### 九、实现退出登录

**1.在ego-sign的TbUserService及实现类中添加方法**

```java
    /**
     * 退出
     */
    EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response);

    @Override
    public EgoResult logout(String token, HttpServletRequest request, HttpServletResponse response) {
        EgoResult result=new EgoResult();
        jedisDaoImpl.del(token);
        CookieUtils.deleteCookie(request,response,"TT_TOKEN");
        result.setStatus(200);
        result.setMsg("OK");
        result.setData(null);
        return result;
    }
```

**2.在ego-sign的TbUserController控制器中添加方法**

```java
    /**
     * 退出登录
     */
    @RequestMapping("user/logout/{token}")
    @ResponseBody
    public Object logout(@PathVariable String token, String callback, HttpServletRequest request, HttpServletResponse response) {
        EgoResult result = tbUserServiceImpl.logout(token, request, response);
        if (callback != null && !callback.equals("")) {
            MappingJacksonValue mjv = new MappingJacksonValue(result);
            mjv.setJsonpFunction(callback);
            return mjv;
        }
        return result;
    }
```

## 第十一天 购物车

### 一、新建ego-cart并完成拦截器功能

**1.搭建ego-cart**

**2.在ego-cart中新建LoginInterceptor拦截器**

```java
public class LoginInterceptor implements HandlerInterceptor {
    @Value("${sign.url}")
    private String signUrl;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        String token = CookieUtils.getCookieValue(httpServletRequest, "TT_TOKEN");
        if (token != null && !token.equals("")) {
            String json = HttpClientUtil.doPost("http://localhost:8084/user/token/" + token);
            EgoResult result = JsonUtils.jsonToPojo(json, EgoResult.class);
            if (result.getStatus() == 200) {
                return true;
            }
        }
        String num = httpServletRequest.getParameter("num");
        httpServletResponse.sendRedirect("http://localhost:8084/user/showLogin?interUrl=" + httpServletRequest.getRequestURL() + "%3Fnum=" + num);
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
```

**3.在ego-cart的springmvc.xml中配置拦截器**

3.1拦截器拦截全部

```xml
    <!--拦截器-->
    <mvc:interceptors>
        <bean class="com.ego.cart.interceptor.LoginInterceptor"/>
    </mvc:interceptors>
```

**4.在ego-sign的TbUserController中修改showLogin方法**

```java
    /**
     * 显示登陆
     */
    @RequestMapping("user/showLogin")
    public String showLogin(@RequestHeader(value = "Referer", defaultValue = "") String url, String interUrl, Model model) {
        if (interUrl!=null&&!interUrl.equals("")) {
            model.addAttribute("redirect", interUrl);
        } else if (!url.equals("")) {
            model.addAttribute("redirect", url);
        }
        return "login";
    }
```

### 二、添加购物车业务

**1.在ego-commons的commons.properties中添加**

```properties
sign.url = http://localhost:8084/user/token/

cart.key = cart:
```

2.在ego-cart中新建CartService及实现类

```java
public interface CartService {
    /**
     * 加入购物车
     */
    void addCart(long id, int num, HttpServletRequest request);
}

@Service
public class CartServiceImpl implements CartService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${sign.url}")
    private String signUrl;
    @Value("${cart.key}")
    private String cartKey;

    @Override
    public void addCart(long id, int num, HttpServletRequest request) {
        //集合中存放购物车中所有商品
        List<TbItemChild> list = new ArrayList<>();
        String key = getToken(request);
        if (jedisDaoImpl.exists(key)) {
            String json = jedisDaoImpl.get(key);
            if (json != null && !json.equals("")) {
                list = JsonUtils.jsonToList(json, TbItemChild.class);
                for (TbItemChild child : list) {
                    if ((long)child.getId() == id) {
                        //购物车中有该商品
                        child.setNum(child.getNum() + num);
                        //重新添加到redis中
                        jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
                        return;
                    }
                }
            }
        }
        TbItemChild child = getChild(id, num);
        list.add(child);
        jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
    }
}
```

**3.在ego-cart中CartController控制器中修改addCart方法**

```java
    /**
     * 添加购物车
     */
    @RequestMapping("cart/add/{id}.html")
    public String addCart(@PathVariable long id, int num, HttpServletRequest request) {
        cartServiceImpl.addCart(id, num, request);
        return "cartSuccess";
    }
```

### 三、显示购物车信息

**1.在ego-cart的CartService及实现类中添加方法**

```java
    /**
     * 显示购物车信息
     */
    List<TbItemChild> showCat(HttpServletRequest request);

    @Override
    public List<TbItemChild> showCat(HttpServletRequest request) {
        String key = getToken(request);
        String json = jedisDaoImpl.get(key);
        return JsonUtils.jsonToList(json,TbItemChild.class);
    }
```

**2.在ego-cart的CartController控制器中添加方法**

```java
    /**
     * 显示购物车
     */
    @RequestMapping("cart/cart.html")
    public String showCart(Model model, HttpServletRequest request) {
        model.addAttribute("cartList", cartServiceImpl.showCat(request));
        return "cart";
    }
```

### 四、修改商品数量

**1.在ego-cart的CartService及实现类中添加方法**

```java
    /**
     * 根据id修改购物车商品数量
     */
    EgoResult update(long id, int num, HttpServletRequest request);

    @Override
    public EgoResult update(long id, int num,HttpServletRequest request) {
        String key = getToken(request);
        String json = jedisDaoImpl.get(key);
        List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
        for (TbItemChild child:list) {
            if ((long)child.getId()==id){
                child.setNum(num);
            }
        }
        String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
        EgoResult result=new EgoResult();
        if (ok.equals("OK")){
            result.setStatus(200);
        }
        return result;
    }
```

**2.在ego-cart的CartController控制器中添加**

```java
    /**
     * 根据id修改购物车商品数量
     */
    @RequestMapping("cart/update/num/{id}/{num}.action")
    @ResponseBody
    public EgoResult update(@PathVariable long id, @PathVariable int num, HttpServletRequest request) {
        return cartServiceImpl.update(id, num, request);
    }
```

### 五、删除购物车中的商品

**1.在ego-cart的CartService及实现类中添加方法**

```java
    /**
     * 根据id删除购物车商品
     */
    EgoResult delete(long id, HttpServletRequest request);

    @Override
    public EgoResult delete(long id, HttpServletRequest request) {
        String key = getToken(request);
        String json = jedisDaoImpl.get(key);
        List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
        TbItemChild itemChild=new TbItemChild();
        for (TbItemChild child:list) {
            if ((long)child.getId()==id){
                itemChild=child;
            }
        }
        list.remove(itemChild);
        String ok = jedisDaoImpl.set(key, JsonUtils.objectToJson(list));
        EgoResult result=new EgoResult();
        if (ok.equals("OK")){
            result.setStatus(200);
        }
        return result;
    }
```

**2.在ego-cart的CartController中添加方法**

```java
    /**
     * 根据id删除购物车商品
     */
    @RequestMapping("cart/delete/{id}.action")
    @ResponseBody
    public EgoResult update(@PathVariable long id, HttpServletRequest request) {
        return cartServiceImpl.delete(id, request);
    }
```

## 第十二天 订单系统实现

### 一、订单需求分析

1.在订单确认页面中从redis中把数据查询出来并显示

1.1确认商品数量

2.提交订单时复杂数据类型传递

2.1向mysql中3表新增

### 二、实现订单确认页面功能

1.在ego-commons的TbItemChild中添加属性

```java
private Boolean enough;
```

2.新建ego-order项目tomcat端口8086

3.在ego-order中添加拦截器，实现登录拦截

4.在ego-order中新建TbOrderService及实现类

```java
public interface TbOrderService {
    /**
     * 确认订单信息包含的商品
     */
    List<TbItemChild> showOrderCart(List<Long> ids, HttpServletRequest request);
}

@Service
public class TbOrderServiceImpl implements TbOrderService {
    @Reference
    private TbItemDubboService tbItemDubboServiceImpl;
    @Resource
    private JedisDao jedisDaoImpl;
    @Value("${cart.key}")
    private String cartKey;
    @Value("${sign.url}")
    private String signUrl;


    @Override
    public List<TbItemChild> showOrderCart(List<Long> ids, HttpServletRequest request) {
        List<TbItemChild> newList = new ArrayList<>();
        String key = getToken(request);
        if (jedisDaoImpl.exists(key)) {
            String json = jedisDaoImpl.get(key);
            List<TbItemChild> list = JsonUtils.jsonToList(json, TbItemChild.class);
            if (list != null && !list.equals("")) {
                for (TbItemChild child : list) {
                    for (Long id : ids) {
                        if ((long) child.getId() == (long) id) {
                            //判断购买数量是否大于库存
                            TbItem item = tbItemDubboServiceImpl.selById(id);
                            if (item.getNum() >= child.getNum()) {
                                child.setEnough(true);
                            } else {
                                child.setEnough(false);
                            }
                            newList.add(child);
                        }
                    }
                }
            }
        }
        return newList;
    }
}
```

5.在ego-order中新建TbOrderController控制器

```java
@Controller
public class OrderController {
    @Resource
    private TbOrderService tbOrderServiceImpl;

    /**
     * 显示订单确认页面
     */
    @RequestMapping("order/order-cart.html")
    public String showOrder(@RequestParam("id") List<Long> ids, Model model, HttpServletRequest request) {
        model.addAttribute("cartList", tbOrderServiceImpl.showOrderCart(ids, request));
        return "order-cart";
    }
}
```

### 三、创建订单

1.在ego-service中新建TbOrderDubboService及实现类

```java
public interface TbOrderDubboService {
    /**
     * 创建订单
     */
    int insOrder(TbOrder order, List<TbOrderItem> list, TbOrderShipping shipping) throws Exception;
}

public class TbOrderDubboServiceImpl implements TbOrderDubboService {
    @Resource
    private TbOrderMapper tbOrderMapper;
    @Resource
    private TbOrderItemMapper tbOrderItemMapper;
    @Resource
    private TbOrderShippingMapper tbOrderShippingMapper;

    @Override
    public int insOrder(TbOrder order, List<TbOrderItem> list, TbOrderShipping shipping) throws Exception {
        int index = tbOrderMapper.insertSelective(order);
        for (TbOrderItem orderItem : list) {
            index += tbOrderItemMapper.insertSelective(orderItem);
        }
        index += tbOrderShippingMapper.insertSelective(shipping);
        if (index==2+list.size()){
            return 1;
        }else{
            throw new Exception("创建订单失败");
        }
    }
}
```

2.在ego-service-impl中注册接口

```xml
    <!--订单-->
    <dubbo:service interface="com.ego.dubbo.service.TbOrderDubboService"
                   ref="tbOrderDubboService"/>
    <bean id="tbOrderDubboService" class="com.ego.dubbo.service.impl.TbOrderDubboServiceImpl"/>
```

3.在ego-order的TbOrderService及实现类中添加方法

```java
    /**
     * 创建订单
     */
    EgoResult create(MyOrderParam param,HttpServletRequest request);

    @Reference
    private TbOrderDubboService tbOrderDubboServiceImpl;
    @Override
    public EgoResult create(MyOrderParam param, HttpServletRequest request) {
        //订单数据
        TbOrder order = new TbOrder();
        order.setPayment(param.getPayment());
        order.setPaymentType(param.getPaymentType());
        long id = IDUtils.genItemId();
        order.setOrderId(id + "");
        Date date = new Date();
        order.setCreateTime(date);
        order.setUpdateTime(date);
        String token = CookieUtils.getCookieValue(request, "TT_TOKEN");
        String jsonUser = HttpClientUtil.doPost(signUrl + token);
        EgoResult egoResult = JsonUtils.jsonToPojo(jsonUser, EgoResult.class);
        Map user = (LinkedHashMap) egoResult.getData();
        order.setUserId(Long.parseLong(user.get("id").toString()));
        order.setBuyerNick(user.get("username").toString());
        order.setBuyerRate(0);
        //订单-商品表
        for (TbOrderItem item : param.getOrderItems()) {
            item.setId(IDUtils.genItemId() + "");
            item.setOrderId(id + "");
        }
        //收货人信息
        TbOrderShipping shopping = param.getOrderShipping();
        shopping.setOrderId(id + "");
        shopping.setCreated(date);
        shopping.setUpdated(date);
        EgoResult result = new EgoResult();
        try {
            int index = tbOrderDubboServiceImpl.insOrder(order, param.getOrderItems(), shopping);
            if (index > 0) {
                result.setStatus(200);
                //删除购买的商品
                String json = jedisDaoImpl.get(cartKey + user.get("username"));
                List<TbItemChild> listCart = JsonUtils.jsonToList(json, TbItemChild.class);
                List<TbItemChild> listNew = new ArrayList<>();
                for (TbItemChild child : listCart) {
                    for (TbOrderItem item : param.getOrderItems()) {
                        if (child.getId().longValue() == Long.parseLong(item.getItemId())) {
                            listNew.add(child);
                        }
                    }
                }
                for (TbItemChild myNew:listNew){
                    listCart.remove(myNew);
                }
                jedisDaoImpl.set(cartKey + user.get("username"), JsonUtils.objectToJson(listCart));
            }
        } catch (Exception e) {
            result.setMsg(e.getMessage());
        }
        return result;
    }
```

4.在ego-order的TbOrderController控制器中添加方法

```java
    /**
     * 创建订单
     */
    @RequestMapping("order/create.html")
    public String createOrder(MyOrderParam param, HttpServletRequest request) {
        EgoResult result = tbOrderServiceImpl.create(param, request);
        if (result.getStatus() == 200) {
            return "my-orders";
        } else {
            request.setAttribute("message", "订单创建失败！");
            return "error/exception";
        }
    }
```



## 第十三天 MySQL读写分离+MyCat分库分表

## 第十四天 项目部署