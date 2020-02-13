import { axios, path } from '../config'


const dealService = {
    registerDeal: (txHash) => {
        const param = { hash: txHash };
        const jwt = sessionStorage.getItem('jwt');

        return axios.post(`${path}/deal/register`, param, {
            headers: { 'x-access-token': jwt }
        }).then(res => {
            return res.status === 200;
        })
    },

    getDealList: () => {
        const jwt = sessionStorage.getItem('jwt');
        return axios.post(`${path}/deal/view`, {/*no param*/ }, {
            headers: { 'x-access-token': jwt }
        }).then(res => {
            return res.data;
        })
    }
};
export default dealService;