<template>
    <Form ref='formValidate' :model='formValidate' :rules='ruleValidate' :label-width='80'>
        <FormItem label='Name' prop='name'>
            <i-Input v-model='formValidate.name' placeholder='Enter your name'></i-Input>
        </FormItem>
        <FormItem label='E-mail' prop='mail'>
            <i-Input v-model='formValidate.mail' placeholder='Enter your e-mail'></i-Input>
        </FormItem>
        <FormItem label='City' prop='city'>
            <Select v-model='formValidate.city' placeholder='Select your city'>
                <Option value='beijing'>New York</Option>
                <Option value='shanghai'>London</Option>
                <Option value='shenzhen'>Sydney</Option>
            </Select>
        </FormItem>
        <FormItem label='Date'>
            <Row>
                <i-Col span='11'>
                    <FormItem prop='date'>
                        <DatePicker type='date' placeholder='Select date' v-model='formValidate.date'></DatePicker>
                    </FormItem>
                </i-Col>
                <i-Col span='2' style='text-align: center'>-</i-Col>
                <i-Col span='11'>
                    <FormItem prop='time'>
                        <TimePicker type='time' placeholder='Select time' v-model='formValidate.time'></TimePicker>
                    </FormItem>
                </i-Col>
            </Row>
        </FormItem>
        <FormItem label='Gender' prop='gender'>
            <RadioGroup v-model='formValidate.gender'>
                <Radio label='male'>Male</Radio>
                <Radio label='female'>Female</Radio>
            </RadioGroup>
        </FormItem>
        <FormItem label='Hobby' prop='interest'>
            <CheckboxGroup v-model='formValidate.interest'>
                <Checkbox label='Eat'></Checkbox>
                <Checkbox label='Sleep'></Checkbox>
                <Checkbox label='Run'></Checkbox>
                <Checkbox label='Movie'></Checkbox>
            </CheckboxGroup>
        </FormItem>
        <FormItem label='Desc' prop='desc'>
            <i-Input v-model='formValidate.desc' type='textarea' :autosize='{minRows: 2,maxRows: 5}' placeholder='Enter something...'></i-Input>
        </FormItem>
        <FormItem>
            <Button type='primary' @click='handleSubmit("formValidate")' style='margin-right: 8px'>Submit</Button>
            <Button @click='handleReset("formValidate")'>Reset</Button>
            <Button type="primary" @click="back()" style='margin-left: 8px'>Back</Button>
        </FormItem>
    </Form>
</template>
<script>
export default {
  data() {
    return {
      formValidate: {
        name: '',
        mail: '',
        city: '',
        gender: '',
        interest: [],
        date: '',
        time: '',
        desc: ''
      },
      ruleValidate: {
        name: [
          {
            required: true,
            message: 'The name cannot be empty',
            trigger: 'blur'
          }
        ],
        mail: [
          {
            required: true,
            message: 'Mailbox cannot be empty',
            trigger: 'blur'
          },
          { type: 'email', message: 'Incorrect email format', trigger: 'blur' }
        ],
        city: [
          {
            required: true,
            message: 'Please select the city',
            trigger: 'change'
          }
        ],
        gender: [
          { required: true, message: 'Please select gender', trigger: 'change' }
        ],
        interest: [
          {
            required: true,
            type: 'array',
            min: 1,
            message: 'Choose at least one hobby',
            trigger: 'change'
          },
          {
            type: 'array',
            max: 2,
            message: 'Choose two hobbies at best',
            trigger: 'change'
          }
        ],
        date: [
          {
            required: true,
            type: 'date',
            message: 'Please select the date',
            trigger: 'change'
          }
        ],
        time: [
          {
            required: true,
            type: 'string',
            message: 'Please select time',
            trigger: 'change'
          }
        ],
        desc: [
          {
            required: true,
            message: 'Please enter a personal introduction',
            trigger: 'blur'
          },
          {
            type: 'string',
            min: 20,
            message: 'Introduce no less than 20 words',
            trigger: 'blur'
          }
        ]
      }
    }
  },
  methods: {
    handleSubmit(name) {
      this.$refs[name].validate(valid => {
        if (valid) {
          this.$Message.success('Success!')
        } else {
          this.$Message.error('Fail!')
        }
      })
    },
    handleReset(name) {
      this.$refs[name].resetFields()
    },
    back() {
      this.$router.go(-1)
    }
  }
}
</script>
