import env from './env'

const DEV_URL = '/manager/'
const PRO_URL = '/manager/'

export default env === 'development' ? DEV_URL : PRO_URL
