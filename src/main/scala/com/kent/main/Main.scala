package com.kent.main

import com.kent.coordinate.Coordinator
import com.kent.coordinate.CoordinatorManager
import com.typesafe.config.ConfigFactory
import akka.actor.ActorSystem
import akka.actor.Props
import com.kent.workflow.WorkFlowManager

object Main extends App{
    val coorStr1 = """
	     <coordinator name="coor1" start="2016-09-10 10:00:00" end="2017-09-10 10:00:00">    
        <trigger>
            <cron config="* * * * * *"/>
        </trigger>
        <workflow path="wf_1"></workflow>
        <param-list>
            <param name="yestoday" value="${time.today|yyyy-MM-dd|-1 day}"/>
            <param name="lastMonth" value="${time.today|yyyyMM|-1 month}"/>
            <param name="yestoday2" value="${time.yestoday}"/>
        </param-list>
    </coordinator>
	    """
    val coorStr2 = """
	     <coordinator name="coor2" start="2016-09-10 10:00:00" end="2017-09-10 10:00:00">    
        <trigger>
            <cron config="* * * * * *"/>
        </trigger>
        <workflow path="wf_2"></workflow>
        <param-list>
            <param name="yestoday" value="${time.today|yyyy-MM-dd|-1 day}"/>
            <param name="lastMonth" value="${time.today|yyyyMM|-1 month}"/>
            <param name="yestoday2" value="${time.yestoday}"/>
        </param-list>
    </coordinator>
	    """
    
    val coorStr3 = """
	     <coordinator name="coor1_1" start="2016-09-10 10:00:00" end="2017-09-10 10:00:00">    
        <trigger>
            <depend-list> 
                <depend wf="wf_1" />
                <depend wf="wf_2" />
            </depend-list>
        </trigger>
        <workflow path="wf_join"></workflow>
        <param-list>
            <param name="yestoday" value="${time.today|yyyy-MM-dd|-1 day}"/>
            <param name="lastMonth" value="${time.today|yyyyMM|-1 month}"/>
            <param name="yestoday2" value="${time.yestoday}"/>
        </param-list>
    </coordinator>
	    """
    val coorStr4 = """
	     <coordinator name="coor1_1" start="2016-09-10 10:00:00" end="2017-09-10 10:00:00">    
        <trigger>
            <cron config="* * * * * *"/>
        </trigger>
        <workflow path="wf_join"></workflow>
        <param-list>
            <param name="yestoday" value="${time.today|yyyy-MM-dd|-1 day}"/>
            <param name="lastMonth" value="${time.today|yyyyMM|-1 month}"/>
            <param name="yestoday2" value="${time.yestoday}"/>
        </param-list>
    </coordinator>
	    """
    var wfStr1 = """
      <work-flow name="wf_1">
          <start name="start_node" to="end_node" />
          <end name="end_node"/>
      </work-flow>
      """
    var wfStr2 = """
      <work-flow name="wf_2">
          <start name="start_node" to="end_node" />
          <end name="end_node"/>
      </work-flow>
      """
    
    var wfStr3 = """
      <work-flow name="wf_join">
          <start name="start_node" to="action_node_1" />
          <action name="action_node_1" retry-times="3" interval="5" timeout="500">
              <host-script>
                  <host>127.0.0.1</host>
                  <script>/home/ouguangneng/1123/run.sh ${lastMonth}</script>
              </host-script>
              <ok to="action_node_2"/>
              <error to="action_node_2"/>
          </action>
          <action name="action_node_2" retry-times="3" interval="5" timeout="500">
              <host-script>
                  <host>127.0.0.1</host>
                  <script>/home/ouguangneng/1123/run.sh ${yestoday}</script>
              </host-script>
              <ok to="end_node"/>
              <error to="end_node"/>
          </action>
          <end name="end_node"/>
      </work-flow>
      """
    
    var wfStr4 = """
      <work-flow name="my_work_flow">
          <start name="start" to="node_1" />
          <action name="node_2" retry-times="3" interval="5" timeout="500">
              <host-script>
                  <host>127.0.0.1</host>
                  <path>/home/ouguangneng/1123/run.sh ${wf.date}</path>
              </host-script>
              <ok to="node_2"/>
              <error to="node_2"/>
          </action>
          <fork name="node_fork">
              <path start="fork_1" />
              <path start="fork_2" />
          </fork>
          <action name="fork_1" retry-times="3" interval="5" timeout="500">
              <host-script>
                  <host>127.0.0.1</host>
                  <path>/home/ouguangneng/1123/run.sh ${wf.date}</path>
              </host-script>
              <ok to="fork_1_2"/>
              <error to="node_2"/>
          </action>
          <action name="fork_1_2" retry-times="3" interval="5" timeout="500">
              <host-script>
                  <host>127.0.0.1</host>
                  <path>/home/ouguangneng/1123/run.sh ${wf.date}</path>
              </host-script>
              <ok to="join_node"/>
              <error to="node_2"/>
          </action>
          <action name="fork_2" retry-times="3" interval="5" timeout="500">
              <host-script>
                  <host>127.0.0.1</host>
                  <path>/home/ouguangneng/1123/run.sh ${wf.date}</path>
              </host-script>
              <ok to="join_node"/>
              <error to="node_2"/>
          </action>
          
          <join name="join_node" to="node_end"/>
          
          <action name="node_end" retry-times="3" interval="5" timeout="500">
              <host-script>
                  <host>127.0.0.1</host>
                  <path>/home/ouguangneng/1123/run.sh ${wf.date}</path>
              </host-script>
              <ok to="join_node"/>
              <error to="end"/>
          </action>
          
          <end name="end"/>
          
          <kill name="node_2">
              <message>nihao a </message>
          </kill>
      </work-flow>
      """
    
    
    val conf = """
    akka {
        actor {
            provider = "akka.remote.RemoteActorRefProvider"
        }
        remote {
            enabled-transports = ["akka.remote.netty.tcp"]
            netty.tcp {
                hostname = "0.0.0.0"
                port = 2551
            }
        }
    }
  """
  import com.kent.coordinate.CoordinatorManager._
  import com.kent.workflow.WorkFlowManager._
  val config = ConfigFactory.parseString(conf)
  val system = ActorSystem("workflow-system", config)
  
  //
  val cmActor = system.actorOf(Props(CoordinatorManager(List())),"cm")
  val wfmActor = system.actorOf(Props(WorkFlowManager(List())),"wfm")
  //cmActor ! Start()
  cmActor ! GetWorkFlowManager(wfmActor)
  wfmActor ! GetCoordinatorManager(cmActor)
/*  cmActor ! AddCoor(coorStr1)
  cmActor ! AddCoor(coorStr2)  
  cmActor ! AddCoor(coorStr3) 
  
  wfmActor ! AddWorkFlow(wfStr1)
  wfmActor ! AddWorkFlow(wfStr2)*/
  
  wfmActor ! AddWorkFlow(wfStr3)
  cmActor ! AddCoor(coorStr4) 
  
  Thread.sleep(3000)
  cmActor ! Start()

}