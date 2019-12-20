# webCourseQuiz
互联网开发前后端分离项目  
环境：idea 2019.3  
##login: 前端代码  
框架：vue   
1. idea下新建一个静态的网页项目，这里我给项目取名为login
2. 依次在命令行项目工作目录下输入以下命令，初始化包结构
    ```vue init webpack login
    cd login
    npm install
    npm run dev
    ```
3. 安装iview（一套基于Vue.js的高质量UI组件库）  
首先先停止vue项目（在控制台按Ctrl+C，再按Y停止项目），停止以后执行命令
    ```
    npm install --save iview
    ```
4. 在main.js中import iview   
   增加代码
    ```
    import iView from 'iview'
    import 'iview/dist/styles/iview.css' // 使用 CSS
    Vue.use(iView)
    ```
    如果main.js代码报错，是因为没有支持ES6的语法，（alt+回车可以转换为ES6的语法）  
    到此为止iview就算是引入完成了，现在重新启动一下项目（npm run dev）就可以来使用iview的样式了。
5. 使用iview
    首先，我们在工程目录componets下新建一个Login.vue文件。并且登录iview的官网找到Form表单组件。复制代码到Login.vue中
    ```
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
    ```
    Login.vue完成后去修改app.vue文件。
    ```
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
    ```
    运行
    ```
    npm run dev
    ```

6. 修改Login.vue代码并且安装和使用axios
    在Login.vue组件中，有一个提交的方法，当校验通过时，会提示“Success!”，失败时会提示“Fail!”,所以我们可以在校验通过的时候提交表单到后台。
    ```
    handleSubmit(name) {
                    this.$refs[name].validate((valid) => {
                        if (valid) {
                            this.$Message.success('Success!');
                        } else {
                            this.$Message.error('Fail!');
                        }
                    })
                }
    ```
    使用axios来发送异步请求。
    首先我们安装axios，并且使用axios。
    安装axios
    在命令行内输入
    ```
    npm install axios -S
    ```
    进行安装。
    安装完成后在main.js中import axios，在main.js中加入以下代码
    ```
    import axios from 'axios'
    Vue.prototype.$axios = axios
    ```
    这样就可以在全局中使用axios做请求了。
    我们在表单校验通过的时候使用axios来请求后台。代码如下
    ```
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
    ```
    到此为止，前端搭建完成  
    
##demo：后端代码   
框架：springboot    
1. 新建一个新的spring initializr工程，使用默认的项目名demo。依赖选择spring web。打开后import maven依赖。
2. 新建LoginController.java 和User.java
    LoginController.java代码如下
    ```
    package com.example.demo;
    
    import org.springframework.web.bind.annotation.RequestBody;
    import org.springframework.web.bind.annotation.RequestMapping;
    import org.springframework.web.bind.annotation.RequestMethod;
    import org.springframework.web.bind.annotation.RestController;

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
    ```
    User.java代码如下
    ```
    package com.example.demo;
    
    import javax.persistence.Entity;
    

    
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
    ```
    由于springboot默认的端口是8080，但是被前端的项目占用了，所以我们需要修改端口，打开src/javaapplication.properties文件加入 server.port=8081即可修改端口号为8081，修改后启动项目。  
    
    Login.vue文件，修改url为localhost:8081/rest/login
    ```
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
    ```

3. 解决跨域问题。
    config下的index.js
    将proxyTable修改为
    ```
    proxyTable: {
        '/rest': {
            target: 'http://localhost:8081',//设置你调用的接口域名和端口号 别忘了加http
            changeOrigin: true,
            pathRewrite: {
                '^/rest': '/rest'
            }
        }
    }
    ```
    并且重新修改提交表单的请求方法为
    ```
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
    ```



