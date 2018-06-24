# WebUrlVideoCast
Android sample using cast sdk v3 to cast video using video urls hosted on server


# Note:
1. If cast button is not appearing in the action bar of this app, please make sure chromecast is connected and discoverable (try rebooting chromecast if needed)

2. Installing this application works fine and can be used as is. This application uses custom receiver (.html) registered in Cast SDK Console. 
Custom Receiver is hosted here: https://snowdonut.herokuapp.com/PeelUrlPlayer.html

3. This application can be used with your own custom receiver. For this, change the **app_id** in **strings.xml** to match the application you registered in Cast SDK Console.

4. If using your own custom receiver, reboot the chromecast once your receiver applciation is ready for testing.


# Custom Receiver html file


<!DOCTYPE html>
<html>
  <head>
    <title>Peel URL Player</title>
  </head>
  <body>
    <cast-media-player></cast-media-player>
    <script src="//www.gstatic.com/cast/sdk/libs/caf_receiver/v3/cast_receiver_framework.js"></script>
    <script type="text/javascript">
      const context = cast.framework.CastReceiverContext.getInstance();
      context.start();
    </script>
  </body>
</html>
