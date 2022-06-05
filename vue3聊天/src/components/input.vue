<template>
	
		
		<el-input
		id="inp"
		:rows="5"
		    v-model="textarea2"
		    type="textarea"
		    placeholder="Please input"
		  />
		  
		  
	
		
	
</template>
	



<script setup lang="ts" >
	
	import { ref } from 'vue'
	const textarea1 = ref('')
	const textarea2 = ref('')
	
</script>


<style>

	#inp{
		position: relative;
		
		
	}
	
	
	
</style>