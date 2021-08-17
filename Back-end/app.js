const express = require("express");
const morgan = require("morgan"); //안녕 자기?
require("dotenv").config();

const indexRouter = require("./routes");
const profileRouter = require("./routes/profile");

const connect = require("./schemas");

const app = express();
connect();

app.set("port", process.env.PORT || 8001);

app.use(morgan("dev"));
app.use(express.json());
app.use(express.urlencoded({ extended: false }));

app.use("/", indexRouter);
app.use("/profile", profileRouter);

app.use((req, res, next) => {
  const err = new Error("Not Found");
  err.status = 404;
  next(err);
});

app.use((err, req, res) => {
  res.locals.message = err.message;
  res.locals.error = req.app.get("env") === "development" ? err : {};
  res.status(err.status || 500);
  res.render("error");
});

app.listen(app.get("port"), () => {
  console.log(app.get("port"), "번 포트에서 대기 중");
});
