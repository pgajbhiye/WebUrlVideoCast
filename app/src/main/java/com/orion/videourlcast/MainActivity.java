package com.orion.videourlcast;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;
import android.widget.VideoView;

import com.google.android.gms.cast.MediaInfo;
import com.google.android.gms.cast.MediaMetadata;
import com.google.android.gms.cast.framework.CastButtonFactory;
import com.google.android.gms.cast.framework.CastContext;
import com.google.android.gms.cast.framework.CastSession;
import com.google.android.gms.cast.framework.SessionManager;
import com.google.android.gms.cast.framework.SessionManagerListener;
import com.google.android.gms.cast.framework.media.RemoteMediaClient;
import com.google.android.gms.common.images.WebImage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MediaCallback {

    private String LOG_TAG = MainActivity.class.getName();
    private SessionManager mSessionManager;
    private CastContext castContext;
    private final SessionManagerListener mSessionManagerListener =
            new SessionManagerListenerImpl();
    private CastSession mCastSession;
    private VideoView videoView;
    String videoUrl = "http://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4";
    String webmUrl = "http://techslides.com/demos/sample-videos/small.webm";
    String ogvUrl = "http://techslides.com/demos/sample-videos/small.ogv";

    Uri uri = Uri.parse(videoUrl);
    StringBuffer videosMetadataFromFile = new StringBuffer();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSessionManager = CastContext.getSharedInstance(this).getSessionManager();
        super.onCreate(savedInstanceState);
        castContext = CastContext.getSharedInstance(this);
        setContentView(R.layout.activity_main);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        try {
            InputStreamReader path = new InputStreamReader(getResources().getAssets().open("videos.json"));
            BufferedReader bufferedInputStream = new BufferedReader(path);
            String input = "";
            while ((input = bufferedInputStream.readLine()) != null) {
                videosMetadataFromFile.append(input);
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "## Error loading file", e);
        }

        try {
            ArrayList<VideoMetaData> videosMetaData = new ArrayList<>();

            JSONObject obj = new JSONObject(videosMetadataFromFile.toString());
            JSONArray array = obj.getJSONArray("categories");
            JSONObject obj1 = (JSONObject) array.get(0);
            JSONArray videosArr = obj1.getJSONArray("videos");
            for (int i = 0; i < videosArr.length(); i++) {
                ArrayList<String> sources = new ArrayList<>();
                JSONObject eachVideo = videosArr.getJSONObject(i);
                sources.add((String) eachVideo.getJSONArray("sources").get(0));
                VideoMetaData metaData = new VideoMetaData(eachVideo.getString("title"), eachVideo.getString("subtitle"),
                        eachVideo.getString("thumb"), sources, eachVideo.getString("description"));
                videosMetaData.add(metaData);
            }

            VideoModel model = new VideoModel("movies", videosMetaData);
            recyclerView.setAdapter(new VideosAdapter(this, model, this));


        } catch (JSONException e) {
            Log.e(LOG_TAG, "## Error parsing file", e);
        }


        videoView = (VideoView) findViewById(R.id.customvideo);
        videoView.setVisibility(View.GONE);
        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        CastButtonFactory.setUpMediaRouteButton(getApplicationContext(),
                menu,
                R.id.media_route_menu_item);
        return true;
    }

    @Override
    protected void onResume() {
        mCastSession = mSessionManager.getCurrentCastSession();
        mSessionManager.addSessionManagerListener(mSessionManagerListener, CastSession.class);
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSessionManager.removeSessionManagerListener(mSessionManagerListener, CastSession.class);
        mCastSession = null;
    }

    @Override
    public void onVideoSelected(VideoMetaData metaData) {
        if (metaData == null) {
            Log.e(LOG_TAG, "## Error no valid metadata available");
            return;
        }
        if (mCastSession == null) {
            Toast.makeText(this, "Click on the cast button to start session", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d(LOG_TAG, "## Video Selected " + metaData.getTitle());
        loadMediaRemote(metaData);
    }


    private class SessionManagerListenerImpl implements SessionManagerListener<CastSession> {
        @Override
        public void onSessionStarting(CastSession session) {
            Log.d(LOG_TAG, "onSessionStarting");
        }

        @Override
        public void onSessionStarted(CastSession session, String s) {
            Log.d(LOG_TAG, "onSessionStarted");
            Toast.makeText(MainActivity.this, " onSessionStarted ", Toast.LENGTH_SHORT).show();
            mCastSession = session;
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionStartFailed(CastSession session, int i) {
            Log.d(LOG_TAG, "onSessionStartFailed");
            Toast.makeText(MainActivity.this, " onSessionStartFailed ", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onSessionEnding(CastSession session) {
            Log.d(LOG_TAG, "onSessionEnding");
        }

        @Override
        public void onSessionEnded(CastSession session, int i) {
            Log.d(LOG_TAG, "onSessionEnded");

        }

        @Override
        public void onSessionResuming(CastSession session, String s) {
            Log.d(LOG_TAG, "onSessionResuming");
        }

        @Override
        public void onSessionResumed(CastSession session, boolean b) {
            Log.d(LOG_TAG, "onSessionResumed");
            invalidateOptionsMenu();
        }

        @Override
        public void onSessionResumeFailed(CastSession session, int i) {
            Log.d(LOG_TAG, "onSessionResumeFailed");

        }

        @Override
        public void onSessionSuspended(CastSession session, int i) {
            Log.d(LOG_TAG, "onSessionSuspended");

        }
    }

    public void loadMediaRemote(VideoMetaData metaData) {
        if (mCastSession == null) return;

        final RemoteMediaClient remoteMediaClient = mCastSession.getRemoteMediaClient();


        MediaMetadata mediaMetadata = new MediaMetadata(MediaMetadata.MEDIA_TYPE_MOVIE);
        mediaMetadata.addImage(new WebImage(Uri.parse(metaData.getThumb())));
        mediaMetadata.putString(MediaMetadata.KEY_TITLE, metaData.getTitle());
        mediaMetadata.putString(MediaMetadata.KEY_SUBTITLE, metaData.getSubtitle());

        MediaInfo mediaInfo = new MediaInfo.Builder(metaData.getSources().get(0))
                .setStreamType(MediaInfo.STREAM_TYPE_BUFFERED)
                .setContentType("video/mp4") //Mention the type based on the video type webmUrl,ogv etc
                .setMetadata(mediaMetadata)
                .build();

        remoteMediaClient.load(mediaInfo, true, 0);
        remoteMediaClient.addListener(new RemoteMediaClient.Listener() {
            @Override
            public void onStatusUpdated() {
                startActivity(new Intent(MainActivity.this, ExpandedControlActivity.class));
                remoteMediaClient.removeListener(this);
            }

            @Override
            public void onMetadataUpdated() {

            }

            @Override
            public void onQueueStatusUpdated() {

            }

            @Override
            public void onPreloadStatusUpdated() {

            }

            @Override
            public void onSendingRemoteMediaRequest() {

            }

            @Override
            public void onAdBreakStatusUpdated() {

            }
        });

    }
}
