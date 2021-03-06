/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package myDemoApp;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.post;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import spark.ModelAndView;
import spark.template.mustache.MustacheTemplateEngine;

public class App 
{
    public String getGreeting() {
        return "Hello world.";
    }

    public static String search(ArrayList<Integer> minMaxArray, int itemCount, int guess) {
      if(minMaxArray == null || minMaxArray.size() <= 1 ) return "Your values are missing";
      if(itemCount == 0) return "You can\'t expect 0 number";
      Random rand = new Random();
      String result = "";
      int min = minMaxArray.get(0);
      int max = minMaxArray.get(1);
      int randomNumber;
      int difference;
      for(int i = 0; i < itemCount; i++){
        randomNumber = rand.nextInt(max - min - 1);
        difference = randomNumber - guess;
        result += "[" + randomNumber + " - " + guess + " = " + difference + "] ";
      }
      return result;
    }

    public static void main(String[] args) {
        Logger logger = LogManager.getLogger(App.class);
        String portString = System.getenv("PORT");
        int port;
        if(portString == null){
          port = 4567;
        }
        else{
          port = Integer.parseInt(System.getenv("PORT"));
        }
        port(port);
        logger.error("Current port number:" + port);

        port(getHerokuAssignedPort());

        get("/", (req, res) -> "Hello, World");

        post("/compute", (req, res) -> {
          //System.out.println(req.queryParams("input1"));
          //System.out.println(req.queryParams("input2"));

          String input1 = req.queryParams("input1");
          java.util.Scanner sc1 = new java.util.Scanner(input1);
          sc1.useDelimiter("[;\r\n]+");
          java.util.ArrayList<Integer> inputList = new java.util.ArrayList<>();
          while (sc1.hasNext())
          {
            int value = Integer.parseInt(sc1.next().replaceAll("\\s",""));
            inputList.add(value);
          }
          sc1.close();
          System.out.println(inputList);


          String input2 = req.queryParams("input2").replaceAll("\\s","");
          int input2AsInt = Integer.parseInt(input2);

          String input3 = req.queryParams("input3").replaceAll("\\s","");
          int input3AsInt = Integer.parseInt(input3);

          String result = App.search(inputList, input2AsInt, input3AsInt);

          Map<String, String> map = new HashMap<String, String>();
          map.put("result", result);
          return new ModelAndView(map, "compute.mustache");
        }, new MustacheTemplateEngine());


        get("/compute",
            (rq, rs) -> {
              Map<String, String> map = new HashMap<String, String>();
              map.put("result", "not computed yet!");
              return new ModelAndView(map, "compute.mustache");
            },
            new MustacheTemplateEngine());
        }

        static int getHerokuAssignedPort() {
            ProcessBuilder processBuilder = new ProcessBuilder();
            if (processBuilder.environment().get("PORT") != null) {
                return Integer.parseInt(processBuilder.environment().get("PORT"));
            }
            return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}
