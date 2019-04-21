package io.andygrove.spark

import org.rogach.scallop._

class Conf(arguments: Seq[String]) extends ScallopConf(arguments) {

  val bench = new Subcommand("bench") {
    val sourcePath = trailArg[String](required = true)
    val sql = trailArg[String](required = true)
    val iterations = trailArg[String](required = false)
  }

  val convert = new Subcommand("convert") {
    val sourcePath = trailArg[String](required = true)
    val destPath = trailArg[String](required = true)
  }

  addSubcommand(bench)
  addSubcommand(convert)
  requireSubcommand()
  verify()
}

object Main {

  def main(args: Array[String]): Unit = {
    val conf = new Conf(args)
    conf.subcommand match {

      case Some(conf.convert) =>
        DataPrep.convertToParquet(conf.convert.sourcePath(), conf.convert.destPath())

      case Some(conf.bench) =>
        Benchmarks.run(conf.bench.sourcePath(), conf.bench.sql(), conf.bench.iterations.getOrElse("1").toInt)

      case _ =>
        println("invalid subcommand")
    }
  }

}

