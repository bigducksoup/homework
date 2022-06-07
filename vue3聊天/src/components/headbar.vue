<template>



	<div id="bar">
		{{ store.state.target.nickname }}
		<el-button type="danger" style="right: 10px;position:absolute;top:7px" @click="clearmsg"> 清空消息 </el-button>
		<el-button style="left: 10px;position:absolute;top:7px" @click="dialogVisible=true"> 添加好友 </el-button>
		<el-button style="position: absolute;right:110px;top:7px;">更新消息</el-button>
	</div>


	<el-dialog v-model="dialogVisible" title="Tips" width="30%" :before-close="handleClose">
		<el-input placeholder="请输入对方账户" v-model="targetaccount"></el-input>
		<template #footer>
			<span class="dialog-footer">
				<el-button @click="dialogVisible = false">Cancel</el-button>
				<el-button type="primary"> 发送 </el-button>
			</span>
		</template>
	</el-dialog>


</template>

<script setup lang="ts">

import { ElMessageBox } from 'element-plus'
import store from '../store';
import {ref} from 'vue';
import axios from 'axios';
import global from '../global';
const dialogVisible = ref(false)

const targetaccount = ref('');

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
	}).catch(e=>{
		console.log(e);
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