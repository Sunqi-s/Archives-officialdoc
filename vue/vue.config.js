const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
    transpileDependencies: true,
    devServer: {
        host: '0.0.0.0',
        port: 8080,
        client: {
            webSocketURL: 'ws://0.0.0.0:8080/ws',

        },
        headers: {
            'Access-Control-Allow-Origin': '*',

        }
    },
    transpileDependencies: true,
    chainWebpack: config =>{
        config.plugin('html')
            .tap(args => {
                args[0].title = "坤鹏智慧公文管理系统";
                return args;
            })
    }
})
