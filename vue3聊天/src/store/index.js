
import {createStore} from 'vuex'
 
// 创建一个新的 store 实例
const store = createStore({
    state() {
        return {
            
            user:{
                account:'',
                nickname:'',
                id:'',
                token:'',
                avatar:''
            },
            
			target:{
                account:'',
                nickname:'',
                id:'',
                avatar:''
            },
            msglist:[
                
            ],
            friendlist:0
			
			
        }
    }
})
 
export default store
