<template>


	<div id="message">

		<el-scrollbar>
			<div id="text" v-for="(item, index) in store.state.msglist">
				<!-- <img v-bind:src="store.state.target.avatar" style="height: 50px;width:50px"  /> -->

				<div v-bind:class="item.id == store.state.user.id ? 't2' : 't1'"> <span style="margin: 10px;">{{
						item.msg
				}}</span> </div>
			</div>
		</el-scrollbar>

	</div>




</template>

<script setup lang="ts">
import global from '../global';
import store from '../store';
import { onMounted, ref, reactive } from 'vue';





const init = () => {
	if (typeof (WebSocket) === "undefined") {
		alert("您的浏览器不支持socket")

	} else {
		let socket = new WebSocket(global.websocketurl + store.state.user.id);
		socket.onopen = open;
		socket.onclose = close;
		socket.onmessage = message;
	}
};

const open = () => {
	console.log("socket连接成功")
}

const close = () => {
	console.log("连接取消");
}

const message = (msg: any) => {
	console.log(msg);
	store.state.msglist.push(JSON.parse(msg.data));

	let m = JSON.parse(msg.data);
	let msgli = localStorage.getItem(store.state.target.id);
	if (msgli != null) {
		let mid = JSON.parse(msgli);
		mid.push(m);
		localStorage.removeItem(store.state.target.id);
		localStorage.setItem(store.state.target.id, JSON.stringify(mid))
	} else {
		let mid = [];
		mid.push(m);
		localStorage.setItem(store.state.target.id, JSON.stringify(mid))
	}
}



onMounted(() => {

	init();

})




</script>

<style>
#message {
	width: 1265px;
	background-color: antiquewhite;
	height: 75%;
	position: absolute;
	left: 170px;
	top: 50px;
}

#text {
	display: flex;
	position: relative;
	width: 1265px;
	background-color: antiquewhite;
	top: 10px;
	margin: 0px;
	padding: 10px 10px 10px 10px;

}

.t2 {
	position: relative;
	float: none;
	left: 1000px;
	width: 200px;
	height: auto;
	background-color: aquamarine;
	border-radius: 20px;
	text-align: center;
	padding: 10px 10px 10px 10px;


}

.t1 {
	float: none;
	position: relative;
	right: -20px;
	width: 200px;
	height: auto;
	background-color: aquamarine;
	border-radius: 20px;
	text-align: center;
	padding: 10px 10px 10px 10px;

}
</style>