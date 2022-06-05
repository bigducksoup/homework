<template>





	<div id="list">

		<el-menu  class="el-menu-vertical-demo" active-text-color="#678933" @select="handleselect">
			<el-menu-item v-for="(item,index) in list"  :index="(index+'')" :key="item.id" >

			<img v-bind:src="item.avatar" style="width: 30px;height:30px;border-radius: 50%; background-color: blueviolet;" />

			&nbsp&nbsp&nbsp
			{{item.nickname}} 
			
			</el-menu-item>



		</el-menu>


	</div>





</template>

<script setup lang="ts">
import { onBeforeMount, onMounted, reactive, ref } from 'vue';
import axios from 'axios';
import global from '../global';
import store from '../store';


const list =ref();




const handleselect = (key:any,keypath:string[],item:any)=>{



	let a = list.value[key];
	store.state.target.account=a.account;
	store.state.target.id=a.id;
	store.state.target.nickname=a.nickname;
	store.state.target.avatar=a.avatar;

	if(localStorage.getItem(a.id)!=null){
		let m = JSON.parse(localStorage.getItem(a.id));
		store.state.msglist = [];
		m.forEach(element => {
			store.state.msglist.push(element);

		});
		console.log(store.state.msglist);
	}else{
		store.state.msglist = [];
	}
	

}



onBeforeMount(() => {
	axios({

		method:'get',
		url:global.baseurl+'/user/friendlist',
		headers:{
			token:store.state.user.token
		}

	}).then(res => {
		if(res.data.code==200){

			list.value=res.data.data;


		}
	}).catch(e => {

	})
})


</script>

<style>
#list {
	width: 170px;
	height: 93%;
	background-color: #ffffff;
}

.item {
	width: 170px;
	height: 50px;
	background-color: azure;
}
</style>