<template>


	<div id="message">

		<el-scrollbar >
			<!-- <div id="message1" style="height:600px;width: 100%;"> -->
			<div id="text" v-for="(item,index) in store.state.msglist">
				<img  v-bind:class="item.id == store.state.user.id? 'avt2':'avt1'"  v-bind:src="item.id == store.state.user.id? store.state.user.avatar:store.state.target.avatar"/>

				 <div v-bind:class="item.id == store.state.user.id ? 't2' : 't1'"> <span v-if="item.type=='text'" >{{
						item.msg
				}}</span> 
				<img v-else style="height: 500px;" :src="item.msg">
				

				</div>			
			</div>
			<!-- </div> -->
					

		</el-scrollbar>

	</div>




</template>

<script setup lang="ts">
import global from '../global';
import store from '../store';
import { onMounted, ref, reactive, watch } from 'vue';
import { mapGetters } from 'vuex';


const content = ref('');




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
	if(msg.data=="连接成功"){
		return;
	}
	let msgcontent = JSON.parse(msg.data)

	if(msgcontent.type=='text'||msgcontent.type=='img'){
	store.state.msglist.push(msgcontent);

	//testing

	


	//back
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
	}else if(msgcontent.type=='friendReauest'){

	}else{

	}
}



onMounted(() => {

	init();

})



</script>

<style>
#message {
	width: 89%;
	background-color: antiquewhite;
	height: 75%;
	position: absolute;
	left: 170px;
	top: 50px;
}

#text {
	/* display: flex; */
	position: relative;
	/* float: inline-start; */
	left: 1px;
	height: auto;
	width: 99%;
	background-color: antiquewhite;
	top: 10px;
	margin: 0px;
	overflow: hidden;

}

.t2 {
	position: relative;
	float: none;
	display: flex;
	top:0;
	right: 20px;
	width: fit-content;
	height: auto;
	background-color: aquamarine;
	border-radius: 5px;
	text-align: center;
	padding: 10px 10px 10px 10px;
	margin: 10px;
	float: right;
	/* margin: 10px; */
	

}


.t1 {
	display: flex;
	float: none;
	position: relative;
	left: 20px;
	top:0;
	width: fit-content;
	height: auto;
	background-color: aquamarine;
	border-radius: 5px;
	text-align: center;
	padding: 10px 10px 10px 10px;
	margin: 10px;
}

.avt2{

	position: relative;
	height: 40px;
	width: 40px;
	border-radius: 50%;
	top: 10px;
	float: right;
}

.avt1{
	 /* style="height: 50px;width:50px;border-radius: 50%;" */
	position: relative;
	height: 40px;
	top:10px;
	width: 40px;
	border-radius: 50%;
	float: left;


}
</style>