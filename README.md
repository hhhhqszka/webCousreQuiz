# webCousreQuiz
互联网开发前后端分离项目
环境：idea 2019.3
login: 前端代码 vue框架
demo：后端代码 springboot
1.新建一个静态的网页项目，这里我给项目取名为login
01.png
02.png
2.初始化包结构
05.png
06.png
3.安装iview
首先先停止vue项目（在控制台按Ctrl+C，再按Y停止项目），停止以后执行命令
npm install --save iview
07.png
4.在main.js中加入iview
加入的代码为
import iView from 'iview'
import 'iview/dist/styles/iview.css' // 使用 CSS
Vue.use(iView)
如果main.js代码报错，是因为没有支持ES6的语法，（alt+回车可以转换为ES6的语法）
08.png

转换为ES6语法，并且引入iview
09.png

到此为止iview就算是引入完成了，现在重新启动一下项目（npm run dev）就可以来使用iview的样式了。
5.使用iview
首先，我们在工程目录componets下新建一个Login.vue文件。并且登录iview的官网找到Form表单组件。复制代码到Login.vue中
10.png

为了方便，我直接贴出代码
<template>
    <Form ref="formInline" :model="formInline" :rules="ruleInline" inline>
        <FormItem prop="userName">
            <Input type="text" v-model="formInline.userName" placeholder="Username">
            <Icon type="ios-person-outline" slot="prepend"></Icon>
            </Input>
        </FormItem>
        <FormItem prop="password">
            <Input type="password" v-model="formInline.password" placeholder="Password">
            <Icon type="ios-lock-outline" slot="prepend"></Icon>
            </Input>
        </FormItem>
        <FormItem>
            <Button type="primary" @click="handleSubmit('formInline')">Signin</Button>
        </FormItem>
    </Form>
</template>
<script>
    export default {
        data() {
            return {
                formInline: {
                    userName: '',
                    password: ''
                },
                ruleInline: {
                    userName: [
                        {required: true, message: 'Please fill in the user name', trigger: 'blur'}
                    ],
                    password: [
                        {required: true, message: 'Please fill in the password.', trigger: 'blur'},
                        {
                            type: 'string',
                            min: 6,
                            message: 'The password length cannot be less than 6 bits',
                            trigger: 'blur'
                        }
                    ]
                }
            }
        },
        methods: {
            handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$Message.success('Success!');
                    } else {
                        this.$Message.error('Fail!');
                    }
                })
            }
        }
    }
</script> 
Login.vue完成后去修改app.vue文件。话不多说，直接上代码。当然，代码内具体的语法需要你自己去了解。

<template>
    <div id="app">
        <Login></Login>
    </div>
</template>
<script>
    import Login from '@/components/Login'
    export default {
        name: 'App',
        components: {
            'Login': Login
        },
        data() {
            return {}
        }
    }
</script>
<style>
    #app {
        text-align: center;
        margin-top:400px ;
    }
</style>
好了，到此为止，vue的登录页面算是完成了。运行来看看效果。


12.png

哈哈，有点太过于单调了。但是页面总是画出来了，也算是成功了。现在我们来修改Login.vue的代码。

6.修改Login.vue代码并且安装和使用axios
在Login.vue组件中，有一个提交的方法，当校验通过时，会提示“Success!”，失败时会提示“Fail!”,所以我们可以在校验通过的时候提交表单到后台。

handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$Message.success('Success!');
                    } else {
                        this.$Message.error('Fail!');
                    }
                })
            }
vue.js官方推荐我们使用axios来发送异步请求。所以我们就使用axios来发送异步请求。
首先我们引入axios，并且使用axios。
安装axios
在命令行内输入
npm install axios -S
进行安装。
安装完成后在main.js中使用axios，在main.js中加入以下代码
import axios from 'axios'
Vue.prototype.$axios = axios
这样就可以在全局中使用axios做请求了。
我们在表单校验通过的时候使用axios来请求后台。代码如下

handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$axios({
                            url: '',//请求的地址
                            method: 'post',//请求的方式
                            data: this.formInline//请求的表单数据
                        }).then(res => {
                            console.info('后台返回的数据', res.data);
                        }).catch(err => {
                            console.info('报错的信息', err.response.message);
                        });
                    } else {
                        this.$Message.error('表单校验失败!');
                    }
                })
            }
到此为止，前端的搭建算是完成了，我们来搭建springboot作为后端，来实现一个简单的登录请求

三、后端（springboot）
1.新建一个新的spring initializr工程，这里我们使用默认的项目名demo。在第三步的时候选择web
13.png

14.png

15.png
2.新建LoginController.java 和User.java
LoginController.java代码如下
package com.example.demo;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * Created by 不爱编程的程序猿
 * 2018/11/8
 */
@RestController
@RequestMapping("/rest")
public class LoginController {

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public Boolean Login(@RequestBody User user) {
        System.out.printf("用户名" + user.getUserName());
        System.out.printf("用户密码" + user.getPassword());
        return Boolean.TRUE;
    }

}

User.java代码如下

package com.example.demo;

import javax.persistence.Entity;

/**
 * Created by 不爱编程的程序猿
 * 2018/11/8
 */

@Entity
public class User {

    //用户名
    private String userName;
    //密码
    private String password;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
由于springboot默认的端口是8080，但是被前端的项目占用了，所以我们需要修改端口，打开application.properties文件加入 server.port=8081即可修改端口号为8081，修改后启动项目。后端项目已经启动，现在我们去前端来请求后端的系统。我们重新回到Login.vue文件，修改url为localhost:8081/rest/login

handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$axios({
                            url: 'localhost:8081/rest/login',//请求的地址
                            method: 'post',//请求的方式
                            data: this.formInline//请求的表单数据
                        }).then(res => {
                            console.info('后台返回的数据', res.data);
                        }).catch(err => {
                            console.info('报错的信息', err.response.message);
                        });
                    } else {
                        this.$Message.error('表单校验失败!');
                    }
                })
            }
打开登录页面输入用户密码以后,发现浏览器的console控制台报错了，查找发现是跨域问题。


16.png

具体什么是跨域问题，这里不做讲解，可以自行百度。
既然出现了跨域问题，自然得想办法解决。我们打开
config下的index.js 打开后可以看到


17.png

我们将proxyTable修改为
proxyTable: {
        '/rest': {
            target: 'http://localhost:8081',//设置你调用的接口域名和端口号 别忘了加http
            changeOrigin: true,
            pathRewrite: {
                '^/rest': '/rest'
            }
        }
    }
并且重新修改提交表单的请求方法为

handleSubmit(name) {
                this.$refs[name].validate((valid) => {
                    if (valid) {
                        this.$axios({
                            url: '/rest/login',//请求的地址
                            method: 'post',//请求的方式
                            data: this.formInline//请求的表单数据
                        }).then(res => {
                            console.info('后台返回的数据', res.data);
                        }).catch(err => {
                            console.info('报错的信息', err.response.message);
                        });
                    } else {
                        this.$Message.error('表单校验失败!');
                    }
                })
            }
在登录页面输入账号：喜欢的话点个赞 密码：6666666 在后台打个断点就可以查看传入的参数。

登录
后台断点

这个项目只是简单的做前后端分离的请求，前端具体如何跳转页面，这里不作讲解，可以去学习vue的router路由。

最后附上工程的目录（没有进行分包）
前端 login工程
前端工程目录
后端工程
后端工程目录

作者：不爱编程的程序员
链接：https://www.jianshu.com/p/bbc455d86a22
来源：简书
著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
