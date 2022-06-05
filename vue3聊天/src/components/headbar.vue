<template>
	
	
		
		<div id="bar">
			{{store.state.target.nickname}}
			
		</div>
		
	
</template>

<script setup lang="ts">

import store from '../store';

</script>

<style >
	#bar{
		width: 1438px;
		height: 50px;
		background-color: #30b5fd;
		text-align: center;
		line-height: 40px;
	}

</style>