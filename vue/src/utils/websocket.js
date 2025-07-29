// src/utils/websocket.js
let socket;
let reconnectTimer;
const RECONNECT_INTERVAL = 5000; // 重连间隔时间，单位：毫秒

export function connectWebSocket(onMessageReceived) {
    // 这里的地址要和后端 WebSocket 服务的地址一致
    socket = new WebSocket('ws://localhost:9090/ws');

    socket.onopen = () => {
        console.log('WebSocket 连接已建立');
        clearTimeout(reconnectTimer);
    };

    socket.onmessage = (event) => {
        const message = event.data;
        // 当接收到消息时，调用传入的回调函数
        if (typeof onMessageReceived === 'function') {
            onMessageReceived(message);
        }
    };

    socket.onclose = () => {
        console.log('WebSocket 连接已关闭，尝试重连...');
        reconnectTimer = setTimeout(() => {
            connectWebSocket(onMessageReceived);
        }, RECONNECT_INTERVAL);
    };

    socket.onerror = (error) => {
        console.error('WebSocket 发生错误:', error);
        socket.close();
    };
}
