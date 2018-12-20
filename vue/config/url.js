import env from './env'

const DEV_URL = '/bc_site_manager/'
const PRO_URL = '/bc_site_manager/'

export default env === 'development' ? DEV_URL : PRO_URL
