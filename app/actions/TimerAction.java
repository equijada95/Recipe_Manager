package actions;

import play.mvc.Action;
import play.mvc.Http;
import play.mvc.Result;

import java.util.concurrent.CompletionStage;

public class TimerAction extends Action.Simple{

    @Override
    public CompletionStage<Result> call(Http.Request request)
    {
        long start = System.currentTimeMillis();
        CompletionStage<Result> ret = this.delegate.call(request);
        long end = System.currentTimeMillis();
        System.out.println(request.toString() + " in " + (end - start) + " ms");
        return ret;
    }
}
