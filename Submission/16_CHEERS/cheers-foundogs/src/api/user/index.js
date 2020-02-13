import { axios, path } from '../config';

const PATH = path;
const userApi = {
    /**
     * 회원가입 함수
     */
    signup(param) {
        // {email, name, password} = param;
        return axios.post(`${PATH}/user/register`, param)
                    .then(res => { return res.status === 200; })
    },
    login(param) {
        // {email, password} = param;
        let result = {};
        axios.post(`${PATH}/user/login`, param)
             .then( res => {
                result.repl = res;

                if(res.status === 200)
                    sessionStorage.setItem('jwt', res.data.token)
             });
        return result;
    },
};
export default userApi;