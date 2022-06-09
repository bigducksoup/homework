<template>



	<div id="bar">
		{{ store.state.target.nickname }}
		<el-button type="danger" style="right: 10px;position:absolute;top:7px" @click="clearmsg"> 清空消息 </el-button>
		<el-button style="left: 10px;position:absolute;top:7px" @click="dialogVisible=true;friendreq()"> 添加好友 </el-button>
		<el-button style="position: absolute;right:110px;top:7px;" @click="refreshmsg">更新消息</el-button>
	</div>


	<el-dialog v-model="dialogVisible" title="Tips" width="50%" :before-close="handleClose">
		<el-input placeholder="请输入对方账户" v-model="targetaccount"></el-input>

		<el-table :data="friendreqlist">
			<el-table-column  prop="requesterAccount" label="账户" width="120" />
			<el-table-column prop="requestTime" label="time" width="120" />
			<el-table-column label="操作" width="120" > 
				<template #default="scope">
					<el-button link type="primary" size="small" @click="handleRequest(scope.$index,true)">接受</el-button>
        			<el-button link type="primary" size="small" @click="handleRequest(scope.$index,false)">拒绝</el-button>
				</template>
			</el-table-column>

		</el-table>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="dialogVisible = false">Cancel</el-button>
				<el-button type="primary" @click="addfriend"> 发送 </el-button>
			</span>
		</template>
	</el-dialog>


</template>

<script setup lang="ts">

import { ElMessageBox } from 'element-plus'
import store from '../store';
import {reactive, ref, watch} from 'vue';
import axios from 'axios';
import global from '../global';

const dialogVisible = ref(false)

const targetaccount = ref('');

const friendreqlist = ref();

const clearmsg = () => {
	localStorage.removeItem(store.state.target.id);
	store.state.msglist = [];
}


const addfriend = () => {
	axios({
		url:global.baseurl+'/user/add',
		method:'post',
		headers:{
			token:store.state.user.token
		},
		params:{
			"targetaccount":targetaccount.value
		}

	}).then(res=>{
		console.log(res);
		targetaccount.value='';
	}).catch(e=>{
		console.log(e);
	})
}


const friendreq = ()=>{
	axios({
		method:'post',
		url:global.baseurl+'/user/requestlist',
		headers:{
			token:store.state.user.token
		}
	}).then(res=>{
		friendreqlist.value = res.data.data;
		console.log(friendreqlist.value)
	}).catch(e=>{

	})
}

const handleClose = (done: () => void) => {
  ElMessageBox.confirm('Are you sure to close this dialog?')
    .then(() => {
      done()
    })
    .catch(() => {
      // catch error
    })
}


const refreshmsg = ()=>{
	axios({
		method:'post',
		headers:{
			token:store.state.user.token
		},
		url:global.baseurl+'/message/history',
		params:{
			"id":store.state.target.id
		}
	}).then(res=>{
		console.log(res)
		if(res.data.code==200){
			let history = res.data.data;
			console.log(history)
			history.forEach(element => {
				
				store.state.msglist.push(element);
			});

		}else{

		}
		
	})




}
const handleRequest = (index:number,ifacc:boolean)=>{
	console.log(friendreqlist.value[index]);
	axios({
		method:'post',
		url:global.baseurl+'/user/opeartion',
		headers:{
			token:store.state.user.token
		},
		params:{
			"ifacc":ifacc,
			"targetId":friendreqlist.value[index].requesterId
		}
	}).then(res=>{
		store.state.friendlist=store.state.friendlist+1
		
	}).catch(e=>{

	});

	
}




</script>

<style >
#bar {
	width: 100%;
	height: 50px;
	background-color: #30b5fd;
	text-align: center;
	line-height: 40px;
}
</style>