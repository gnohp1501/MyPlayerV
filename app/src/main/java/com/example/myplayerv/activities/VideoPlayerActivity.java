package com.example.myplayerv.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.audiofx.AudioEffect;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myplayerv.R;
import com.example.myplayerv.adapters.IconAdapter;
import com.example.myplayerv.adapters.VideoFileTabAdapter;
import com.example.myplayerv.dialog.BrightnessDialog;
import com.example.myplayerv.dialog.PlaylistDialog;
import com.example.myplayerv.dialog.VolumeDialog;
import com.example.myplayerv.entities.Icon;
import com.example.myplayerv.entities.MediaFiles;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.PlaybackException;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.ProgressiveMediaSource;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.io.File;
import java.util.ArrayList;

public class VideoPlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private SimpleExoPlayer player;
    private PlayerView playerView;
    public int position;
    private String title;
    private ImageView exo_fullscreen, prev, reply, forward, next, lock, unlock, iv_back,iv_playlist;
    private ConstraintLayout playerViewExo;
    private ArrayList<MediaFiles> mediaFiles = new ArrayList<>();
    private TextView tvTitle;
    private ConcatenatingMediaSource concatenatingMediaSource;
    boolean fullscreen = false;
    private final ArrayList<Icon> mIcon = new ArrayList<>();
    private IconAdapter iconAdapter;
    private RecyclerView recyclerViewIcon;
    private View nightMode;
    private boolean expand = false;
    private boolean dark = false;
    private boolean mute = false;
    private boolean isFavorite = false;
    private PlaybackParameters parameters;
    private float speed ;
    BottomSheetDialog bottomSheetDialog;
    private VideoFileTabAdapter videoFileTabAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);
        init();
        playVideo();
        setExo_fullscreen();
        setExo_lock();
        setExo_unlock();
        setIconApp();
        exoBack();
        //
        prev.setOnClickListener(this);
        next.setOnClickListener(this);
        //

    }

    private void setIconApp() {

        mIcon.add(new Icon(R.drawable.ic_baseline_arrow_right_24, ""));
        mIcon.add(new Icon(R.drawable.ic_baseline_favorite_border_24,""));
        mIcon.add(new Icon(R.drawable.ic_vol_mute, "mute"));
        mIcon.add(new Icon(R.drawable.ic_baseline_nights_stay_24, "night"));
    }

    public void init() {
        getDataVideo();
        playerView = findViewById(R.id.exoplayer_view);
        tvTitle = findViewById(R.id.tv_titleVideo);
        tvTitle.setText(title.replace(".mp4",""));
        iv_playlist=findViewById(R.id.iv_playlist);
        exo_fullscreen = findViewById(R.id.exo_fullscreen);
        prev = findViewById(R.id.exo_prev);
        reply = findViewById(R.id.exo_rew);
        forward = findViewById(R.id.exo_ffwd);
        next = findViewById(R.id.exo_next);
        lock = findViewById(R.id.exo_lock);
        unlock = findViewById(R.id.exo_unlock);
        playerViewExo = findViewById(R.id.playerView);
        nightMode = findViewById(R.id.nightmode);
        iv_back = findViewById(R.id.iv_back);
        recyclerViewIcon = findViewById(R.id.recyclerview_icon);
        setDataRecyclerview();
        menuTop();
        playlistView();
    }
    private void playlistView(){
        iv_playlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                bottomSheetDialog = new BottomSheetDialog(VideoPlayerActivity.this,R.style.BottomSheetTheme);
//                View bsView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.videolist_bs_layout,
//                        findViewById(R.id.bottom_sheet));
//                bottomSheetDialog.setContentView(bsView);
//                bottomSheetDialog.show();
                PlaylistDialog playlistDialog = new PlaylistDialog(mediaFiles,videoFileTabAdapter);

                playlistDialog.show(getSupportFragmentManager(),playlistDialog.getTag());

            }
        });

    }

    private void setDataRecyclerview() {
        iconAdapter = new IconAdapter(mIcon, this);
        LinearLayoutManager linearLayout = new LinearLayoutManager(getApplicationContext(),
                RecyclerView.HORIZONTAL, true);
        recyclerViewIcon.setLayoutManager(linearLayout);
        recyclerViewIcon.setAdapter(iconAdapter);
        iconAdapter.notifyDataSetChanged();
    }

    private void getDataVideo() {

        position = getIntent().getIntExtra("position", 1);
        title = getIntent().getStringExtra("video_title");
        mediaFiles = getIntent().getExtras().getParcelableArrayList("videoArrayList");
    }

    private void menuTop() {
        iconAdapter.setOnItemClickListener(new IconAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos) {
                if (pos == 0) {
                    if (expand) {
                        mIcon.clear();
                        setIconApp();
                        iconAdapter.notifyDataSetChanged();
                        expand = false;
                    } else {
                        if (mIcon.size() == 4) {
                            mIcon.add(new Icon(R.drawable.ic_baseline_volume_down_24, "volumn"));
                            mIcon.add(new Icon(R.drawable.ic_baseline_brightness_5_24, "brightness"));
                            mIcon.add(new Icon(R.drawable.ic_baseline_equalizer_24, "equalizer"));
                            mIcon.add(new Icon(R.drawable.ic_baseline_speed_24, "speed"));
                            mIcon.add(new Icon(R.drawable.ic_baseline_add_circle_outline_24, "speed"));
                        }
                        mIcon.set(pos, new Icon(R.drawable.ic_baseline_arrow_left_24, ""));
                        iconAdapter.notifyDataSetChanged();
                        expand = true;
                    }
                }
                if (pos ==1){
                    if(isFavorite){
                        mIcon.set(pos, new Icon(R.drawable.ic_baseline_favorite_border_24, ""));
                        iconAdapter.notifyDataSetChanged();
                        isFavorite = false;
                    }else {
                        mIcon.set(pos, new Icon(R.drawable.ic_baseline_favorite_24, ""));
                        iconAdapter.notifyDataSetChanged();
                        isFavorite = true;
                    }

                }
                if (pos == 2) {
                    if (mute) {
                        player.setVolume(1);
                        mIcon.set(pos, new Icon(R.drawable.ic_vol_mute, "mute"));
                        iconAdapter.notifyDataSetChanged();
                        mute = false;
                    } else {
                        player.setVolume(0);
                        mIcon.set(pos, new Icon(R.drawable.ic_vol_unmute, "mute"));
                        iconAdapter.notifyDataSetChanged();
                        mute = true;
                    }
                }

                if (pos == 3) {
                    if (dark) {
                        nightMode.setVisibility(View.GONE);
                        mIcon.set(pos, new Icon(R.drawable.ic_baseline_nights_stay_24, "NIGHT"));
                        iconAdapter.notifyDataSetChanged();
                        dark = false;
                    } else {
                        nightMode.setVisibility(View.VISIBLE);
                        mIcon.set(pos, new Icon(R.drawable.ic_baseline_brightness_1_24, "DAY"));
                        iconAdapter.notifyDataSetChanged();
                        dark = true;
                    }
                }
                if (pos == 4) {
                    VolumeDialog volumeDialog = new VolumeDialog();
                    volumeDialog.show(getSupportFragmentManager(), "dialog");
                    iconAdapter.notifyDataSetChanged();
                }
                if (pos == 5) {
                    BrightnessDialog brightness = new BrightnessDialog();
                    brightness.show(getSupportFragmentManager(), "dialog");
                    iconAdapter.notifyDataSetChanged();
                }
                if (pos == 6) {
                    Intent intent = new Intent(AudioEffect.ACTION_DISPLAY_AUDIO_EFFECT_CONTROL_PANEL);
                    if ((intent.resolveActivity(getPackageManager()) != null)) {
                        startActivityForResult(intent, 123);
                    } else {
                        Toast.makeText(getApplicationContext(), "Hee", Toast.LENGTH_SHORT).show();
                    }
                    iconAdapter.notifyDataSetChanged();
                }
                if (pos == 7) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(VideoPlayerActivity.this);
                    alert.setTitle("Select speed").setPositiveButton("OKE",null);
                    String[] items = {"0.5x","Normal Speed : 1.x","1.25x","1.5x","2.x"};
                    int checkedItem = -1;
                    alert.setSingleChoiceItems(items, checkedItem, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    speed = 0.5f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 1:
                                    speed = 1f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 2:
                                    speed = 1.25f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 3:
                                    speed = 1.5f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                case 4:
                                    speed = 2f;
                                    parameters = new PlaybackParameters(speed);
                                    player.setPlaybackParameters(parameters);
                                    break;
                                default:
                                    break;
                            }
                        }
                    });
                    AlertDialog alertDialog = alert.create();
                    alertDialog.show();
                }
            }
        });
    }

    private void exoBack() {
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (player != null) {
                    player.release();
                }
                finish();
            }
        });
    }

    private void playVideo() {
        String path = mediaFiles.get(position).getPath();
        Uri uri = Uri.parse(path);
        player = new SimpleExoPlayer.Builder(this)
                .setSeekBackIncrementMs(10000)
                .setSeekForwardIncrementMs(10000).build();
        DefaultDataSourceFactory dataSourceFactory = new DefaultDataSourceFactory(
                this, Util.getUserAgent(this, "app"));
        concatenatingMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < mediaFiles.size(); i++) {
            new File(String.valueOf(mediaFiles.get(i)));
            MediaSource mediaSource = new ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(Uri.parse(String.valueOf(uri)));
            concatenatingMediaSource.addMediaSource(mediaSource);
        }
        playerView.setPlayer(player);
        playerView.setKeepScreenOn(true);
        player.setPlaybackParameters(parameters);
        player.prepare(concatenatingMediaSource);
        player.seekTo(position, C.TIME_UNSET);
        playerError();
    }

    private void setExo_lock() {
        lock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerViewExo.setVisibility(View.GONE);
                unlock.setVisibility(View.VISIBLE);
            }
        });
    }

    private void setExo_unlock() {
        unlock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playerViewExo.setVisibility(View.VISIBLE);
                lock.setVisibility(View.VISIBLE);
                unlock.setVisibility(View.GONE);
            }
        });
    }

    private void setExo_fullscreen() {
        exo_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fullscreen) {
                    exo_fullscreen.setImageDrawable(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.ic_fullscreen));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_VISIBLE);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().show();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    playerView.setLayoutParams(params);
                    fullscreen = false;
                } else {
                    exo_fullscreen.setImageDrawable(ContextCompat.getDrawable(VideoPlayerActivity.this, R.drawable.ic_fullscreenexit));
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                    if (getSupportActionBar() != null) {
                        getSupportActionBar().hide();
                    }
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) playerView.getLayoutParams();
                    params.width = ViewGroup.LayoutParams.MATCH_PARENT;
                    params.height = ViewGroup.LayoutParams.MATCH_PARENT;
                    playerView.setLayoutParams(params);
                    fullscreen = true;
                }
            }
        });
    }

    private void playerError() {
        player.addListener(new Player.Listener() {
            @Override
            public void onPlayerError(PlaybackException error) {
                Toast.makeText(getApplicationContext(), "HE", Toast.LENGTH_SHORT).show();
            }
        });
        player.setPlayWhenReady(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (player.isPlaying()) {
            player.stop();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        player.setPlayWhenReady(false);
        player.getPlaybackState();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        player.setPlayWhenReady(true);
        player.getPlaybackState();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exo_prev:
                try {
                    player.stop();
                    position--;
                    playVideo();
                    tvTitle.setText(mediaFiles.get(position).getTitle());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No video next", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.exo_next:
                try {
                    player.stop();
                    position++;
                    playVideo();
                    tvTitle.setText(mediaFiles.get(position).getTitle());
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "No video next", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
