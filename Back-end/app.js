const express = require('express')
const morgan = require('morgan')

const indexRouter = require('./routes')

const app = express()

app.set('port', process.env.PORT || 8001)

app.use(morgan('dev'))
app.use(express.json())
app.use(express.urlencoded({extended: false}))

app.use('/', indexRouter)

app.listen(app.get('port'), ()=>{
  console.log(app.get('port'),'번 포트에서 대기 중')
})

