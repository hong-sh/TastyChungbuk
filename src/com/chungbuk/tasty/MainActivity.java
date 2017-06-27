package com.chungbuk.tasty;

import java.util.ArrayList;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import adapter.DrawerAdapter;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.DialogFragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks;
import com.google.android.gms.common.GooglePlayServicesClient.OnConnectionFailedListener;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import common.ActivityManager;
import common.WebServiceUtil;
import dto.StoreInfo;
import dto.UserInfo;

@SuppressLint("NewApi")
public class MainActivity extends ActionBarActivity implements
      ConnectionCallbacks, OnConnectionFailedListener,
      com.google.android.gms.location.LocationListener,
      DialogFilter.FilterListener {

   private ArrayList<String> navItems = new ArrayList<String>();
   private ListView lvNavList;
   private DrawerLayout dlDrawer;
   private ActionBarDrawerToggle dtToggle;

   private ProgressBar progressbar;

   private GoogleMap map;
   /**
    * Main entry point for location related APIs, such as location and geofence
    */
   private LocationClient locationClient;
   private static final LocationRequest REQUEST = LocationRequest.create()
         .setInterval(5000).setFastestInterval(16)
         .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
   /**
    * Default location to point the map, e.g. Seoul
    */
   private LatLng seoul = new LatLng(36.635682, 127.491401);
   private LocationManager locationManager;
   private Location myLocation;

   private boolean locationChanged;

   private float zoomNormal = 15.0f;

   // private ProgressDialog myProgressDialog;
   private static final int GPS_SETTING_REQUEST_CODE = 100;

   // public static ArrayList<HashMap<String, Object>> AllstoreList;
   public static ArrayList<StoreInfo> all_list = new ArrayList<StoreInfo>();

   private boolean filter_dialog_call = false;
   private String[] filter_flag = new String[] { "0", "1", "2", "3", "4" };

   private ActivityManager act = ActivityManager.getInstance();
   private UserInfo user_info;
   private String[] info;

   @SuppressLint("NewApi")
   @Override
   protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.activity_main);

      act.addlist(this);

      setupActionBar();
      init();
      checkGps();
      setUpLocationClientIfNeeded();
      setUpMapIfNeeded();
   }

   /**
    * Set up the
    * {@link android.support.v7.app.ActionBarActivity.getSupportActionBar}.
    */
   private void setupActionBar() {
      getSupportActionBar().setDisplayHomeAsUpEnabled(true);
      getSupportActionBar().setHomeButtonEnabled(true);

   }

   private void init() {

      user_info = new UserInfo(this);
      info = user_info.loadLoginData();

      lvNavList = (ListView) findViewById(R.id.lv_activity_main_nav_list);
      locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

      ((Button) findViewById(R.id.zoom_in_btn))
            .setOnClickListener(setLocationButtonClickListener);
      ((Button) findViewById(R.id.zoom_out_btn))
            .setOnClickListener(setLocationButtonClickListener);
      ((Button) findViewById(R.id.find_loc_btn))
            .setOnClickListener(setLocationButtonClickListener);
      progressbar = (ProgressBar) findViewById(R.id.progressBar);
      setupDrawer();
   }

   /**
    * Click listener for the buttons
    */
   View.OnClickListener setLocationButtonClickListener = new View.OnClickListener() {

      @Override
      public void onClick(View v) {
         switch (v.getId()) {
         case R.id.zoom_in_btn:
            zoomIn();
            break;
         case R.id.zoom_out_btn:
            zoomOut();
            break;
         case R.id.find_loc_btn:
            findMyLocation();
            break;
         default:
            break;
         }

      }
   };

   /**
    * Overrides the {@link GoogleMap}'s zoom in method.
    */
   private void zoomIn() {
      map.animateCamera(CameraUpdateFactory.zoomBy(1.0f));

   }

   /**
    * Overrides the {@link GoogleMap}'s zoom out method.
    */
   private void zoomOut() {
      map.animateCamera(CameraUpdateFactory.zoomBy(-1.0f));
   }

   /**
    * Overrides the {@link GoogleMap}'s find my location method.
    */
   private void findMyLocation() {
      Log.i("myTag3", "findMyLocation");
      // sl_Animation.LoadViewAnimation(AnimationActionState.MAP_SEARCH);
      double lat = 0, lng = 0;
      if (myLocation != null) {
         lat = myLocation.getLatitude();
         lng = myLocation.getLongitude();
      } else {
         if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
               || locationManager
                     .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (locationManager
                  .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
               lat = locationManager.getLastKnownLocation(
                     LocationManager.GPS_PROVIDER).getLatitude();
               lng = locationManager.getLastKnownLocation(
                     LocationManager.GPS_PROVIDER).getLongitude();

            } else if (locationManager
                  .getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
               lat = locationManager.getLastKnownLocation(
                     LocationManager.NETWORK_PROVIDER).getLatitude();
               lng = locationManager.getLastKnownLocation(
                     LocationManager.NETWORK_PROVIDER).getLongitude();
            }
         }
      }
      map.animateCamera(
            CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),
                  map.getCameraPosition().zoom), 800, null);
   }

   private void setupDrawer() {

      // private String[] navItems = { "MyPage","즐겨찾기","이용안내" };
      navItems.add("MyPage");
      navItems.add("즐겨찾기");
      navItems.add("이용안내");
      navItems.add("로그아웃");

      View header = getLayoutInflater().inflate(
            R.layout.listview_drawer_header, null, false);
      String[] user_info_str = new String[5];
      user_info_str = user_info.loadLoginData();

      lvNavList.addHeaderView(header);
      ((TextView) header.findViewById(R.id.textView_name))
            .setText(user_info_str[2]);
      if (info[3].equals("0")) {

         ((ImageView) header.findViewById(R.id.imageView_user))
               .setImageResource(R.drawable.icon_man);
      } else {
         ((ImageView) header.findViewById(R.id.imageView_user))
               .setImageResource(R.drawable.icon_woman);
      }
      lvNavList.setAdapter(new DrawerAdapter(this, R.layout.listview_drawer,
            navItems));
      lvNavList.setOnItemClickListener(new DrawerItemClickListener());

      dlDrawer = (DrawerLayout) findViewById(R.id.dl_activity_main_drawer);
      dtToggle = new ActionBarDrawerToggle(this, dlDrawer,
            R.drawable.ic_menu, R.string.open_drawer, R.string.close_drawer) {

         @Override
         public void onDrawerClosed(View drawerView) {
            super.onDrawerClosed(drawerView);
         }

         @Override
         public void onDrawerOpened(View drawerView) {
            super.onDrawerOpened(drawerView);
         }

      };
      dlDrawer.setDrawerListener(dtToggle);
      getActionBar().setDisplayHomeAsUpEnabled(true);

   }

   private class DrawerItemClickListener implements
         ListView.OnItemClickListener {

      @Override
      public void onItemClick(AdapterView<?> adapter, View view,
            int position, long id) {
         Intent intent;
         switch (position) {
         case 0:

            break;
         case 1:
            intent = new Intent(getApplicationContext(),
                  MyPageActivity.class);
            act.removeAct(MainActivity.this);
            startActivity(intent);
            
            dlDrawer.closeDrawer(lvNavList);
            break;
         case 2:
            intent = new Intent(getApplicationContext(),
                  FavoriteActivity.class);
            act.removeAct(MainActivity.this);
            startActivity(intent);
            dlDrawer.closeDrawer(lvNavList);
            break;
         case 3:
        	 intent = new Intent(getApplicationContext(),
                     GuideActivity.class);
             act.removeAct(MainActivity.this);
               startActivity(intent);
            dlDrawer.closeDrawer(lvNavList);
            break;
         case 4:
            AlertDialog.Builder dialog = new AlertDialog.Builder(
                  MainActivity.this);
            dialog.setTitle("로그아웃 확인");
            dialog.setMessage("정말 로그아웃 하시겠습니까?");
            dialog.setCancelable(false);
            dialog.setPositiveButton("확인",
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog,
                           int which) {
                        SharedPreferences pref = getSharedPreferences(
                              "user_info", MODE_PRIVATE);
                        SharedPreferences.Editor editor = pref.edit();
                        editor.clear();
                        editor.commit();
                        act.Allremove(MainActivity.this);
                        finish();
                     }
                  });
            dialog.setNegativeButton("취소",
                  new DialogInterface.OnClickListener() {
                     public void onClick(DialogInterface dialog,
                           int which) {
                        dialog.dismiss();
                     }
                  });
            dialog.show();

            dlDrawer.closeDrawer(lvNavList);
            break;
         }

      }

   }

   @Override
   protected void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);

      if (requestCode == GPS_SETTING_REQUEST_CODE) {
         if (locationManager
               .isProviderEnabled(LocationManager.NETWORK_PROVIDER)
               && locationManager
                     .isProviderEnabled(LocationManager.GPS_PROVIDER)) {

            // myProgressDialog = ProgressDialog.show(MainActivity.this, "",
            // " 현재 위치를 가져오는 중 입니다...  ", true, true);
            // new StoreListAsyncTask().execute();

            progressbar.setVisibility(View.VISIBLE);
         } else {
            goGpsSettings();
         }
      }

   }

   private void checkGps() {

      if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
            && locationManager
                  .isProviderEnabled(LocationManager.GPS_PROVIDER)) {
         // myProgressDialog = ProgressDialog.show(MainActivity.this, "",
         // " 현재 위치를 가져오는 중 입니다...  ", true, true);
         progressbar.setVisibility(View.VISIBLE);
         // new StoreListAsyncTask().execute();
      } else {
         goGpsSettings();
      }

   }

   private void goGpsSettings() {
      AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
      dialog.setTitle("GPS 설정");
      dialog.setMessage(" GPS 위성 사용에 체크해야 서비스를 원할하게 사용할 수 있습니다.\n GPS 설정을 변경하시겠습니까?");
      dialog.setCancelable(false);
      dialog.setPositiveButton("GPS 설정",
            new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                  dialog.dismiss();
                  startActivityForResult(
                        new Intent(
                              android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS),
                        GPS_SETTING_REQUEST_CODE);
               }
            });
      dialog.setNegativeButton("프로그램 종료",
            new DialogInterface.OnClickListener() {
               public void onClick(DialogInterface dialog, int which) {
                  MainActivity.this.finish();
               }
            });
      dialog.show();
   }

   @Override
   public boolean onCreateOptionsMenu(Menu menu) {
      // Inflate the menu; this adds items to the action bar if it is present.
      getMenuInflater().inflate(R.menu.main, menu);
      return true;
   }

   protected void onPostCreate(Bundle savedInstanceState) {
      super.onPostCreate(savedInstanceState);
      dtToggle.syncState();
   }

   @Override
   public void onConfigurationChanged(Configuration newConfig) {
      super.onConfigurationChanged(newConfig);
      dtToggle.onConfigurationChanged(newConfig);
   }

   @Override
   public boolean onOptionsItemSelected(MenuItem item) {
      if (dtToggle.onOptionsItemSelected(item)) {
         return true;
      }
      if (item.getItemId() == R.id.action_filter) {
         filter_dialog_call = true;
         DialogFragment fragment = DialogFilter.newInstance();
         fragment.setStyle(
               DialogFragment.STYLE_NO_TITLE,
               android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth);
         fragment.show(getSupportFragmentManager(), "dialog");

      } else {
    	 if(all_list.size() ==0)
    	 {
    		 Toast.makeText(this, "맛집 리스트를 받아오는 중입니다.",
                     Toast.LENGTH_SHORT).show();
    	 }
    	 else
    	 {
    		 Intent intent = new Intent(MainActivity.this,
    	               ListViewActivity.class);
    	         startActivity(intent);
    	 }
    		 
      }
      return super.onOptionsItemSelected(item);
   }

   /**
    * Set up the location client listener.
    */
   private void setUpLocationClientIfNeeded() {
      if (locationClient == null) {
         locationClient = new LocationClient(getApplicationContext(), this, // ConnectionCallbacks
               this); // OnConnectionFailedListener
         locationClient.connect();
      }
   }

   /**
    * Set up the map.
    */
   private void setUpMapIfNeeded() {
      if (GooglePlayServicesUtil
            .isGooglePlayServicesAvailable(MainActivity.this) != ConnectionResult.SUCCESS) {
         GooglePlayServicesUtil
               .getErrorDialog(
                     GooglePlayServicesUtil
                           .isGooglePlayServicesAvailable(MainActivity.this),
                     MainActivity.this, 10, new OnCancelListener() {
                        public void onCancel(DialogInterface dialog) {
                           finish();
                        }
                     }).show();
      } else {
         if (map == null) {
            // Try to obtain the map from the SupportMapFragment.
            map = ((SupportMapFragment) getSupportFragmentManager()
                  .findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (map != null) {
               map.setMyLocationEnabled(true);
               map.getUiSettings().setZoomGesturesEnabled(true);
               map.getUiSettings().setZoomControlsEnabled(false);
               map.getUiSettings().setScrollGesturesEnabled(true);
               map.getUiSettings().setMyLocationButtonEnabled(false);
               // map.setOnCameraChangeListener(mapCameraChangeListener);
               map.setOnMarkerClickListener(markerClickListener);
               map.setOnInfoWindowClickListener(new OnInfoWindowClickListener() {

                  @Override
                  public void onInfoWindowClick(Marker marker) {
                     // TODO Auto-generated method stub

                     StoreInfo item = new StoreInfo();
                     item = all_list.get(Integer.parseInt(marker
                           .getSnippet()));
                     Intent intent = new Intent(MainActivity.this,
                           DetailPageActivity.class);
                     intent.putExtra("ipx", item.get_ipx());
                     startActivity(intent);

                  }
               });
               map.setInfoWindowAdapter(new InfoWindowAdapter() {
                  @Override
                  public View getInfoWindow(Marker marker) {

                     return null;
                  }

                  public View getInfoContents(Marker marker) {

                     View v = getLayoutInflater().inflate(
                           R.layout.bullon, null);
                     TextView name = (TextView) v
                           .findViewById(R.id.textView_name);
                     TextView review_count = (TextView) v
                           .findViewById(R.id.textView_review_count);
                     ((RatingBar) v.findViewById(R.id.star_ratingbar))
                           .setRating((float) ((float) markerselect_item
                                 .get_rate() / 2.0));
                     name.setText(markerselect_item.get_name());
                     review_count.setText(String
                           .valueOf(markerselect_item
                                 .get_review_count()));
                     ImageView icon;
                     switch (Integer.parseInt(markerselect_item
                           .get_type())) {
                     case 0:
                        ((ImageView) v
                              .findViewById(R.id.imageView_main))
                              .setImageResource(R.drawable.ballon_kor);
                        break;
                     case 1:
                        ((ImageView) v
                              .findViewById(R.id.imageView_main))
                              .setImageResource(R.drawable.ballon_chinese);
                        break;
                     case 2:
                        ((ImageView) v
                              .findViewById(R.id.imageView_main))
                              .setImageResource(R.drawable.ballon_japan);
                        break;
                     case 3:
                        ((ImageView) v
                              .findViewById(R.id.imageView_main))
                              .setImageResource(R.drawable.ballon_eng);
                        break;
                     case 4:
                        ((ImageView) v
                              .findViewById(R.id.imageView_main))
                              .setImageResource(R.drawable.ballon_etc);
                        break;
                     }
                     return v;
                  }
               });
               // map.setOnMapClickListener(mapClickListener);

               autoLocationStart();
            }
         }
      }
   }

   /**
    * Find user's current location with a {@link CountDownTimer}.
    */
   private void autoLocationStart() {
      double lat = seoul.latitude;
      double lng = seoul.longitude;

      if (locationChanged) {
         lat = myLocation.getLatitude();
         lng = myLocation.getLongitude();
      }

      if (lat == seoul.latitude || lng == seoul.latitude) {

         if (lat == 0 || lng == 0) {
            lat = seoul.latitude;
            lng = seoul.longitude;
         }

      }

      if (lat == seoul.latitude || lng == seoul.latitude) {
         if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
               || locationManager
                     .isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            if (locationManager
                  .getLastKnownLocation(LocationManager.GPS_PROVIDER) != null) {
               lat = locationManager.getLastKnownLocation(
                     LocationManager.GPS_PROVIDER).getLatitude();
               lng = locationManager.getLastKnownLocation(
                     LocationManager.GPS_PROVIDER).getLongitude();

            } else if (locationManager
                  .getLastKnownLocation(LocationManager.NETWORK_PROVIDER) != null) {
               lat = locationManager.getLastKnownLocation(
                     LocationManager.NETWORK_PROVIDER).getLatitude();
               lng = locationManager.getLastKnownLocation(
                     LocationManager.NETWORK_PROVIDER).getLongitude();
            }
         }
      }

      autoLocationEnd(lat, lng);
   }

   /**
    * Animate map to move to user's current location.
    */
   private void autoLocationEnd(double lat, double lng) {
      map.animateCamera(CameraUpdateFactory.newLatLngZoom(
            new LatLng(lat, lng), zoomNormal), 400, null);

   }

   @Override
   public void onConnectionFailed(ConnectionResult arg0) {
      // TODO Auto-generated method stub

   }

   @Override
   public void onConnected(Bundle arg0) {
      // TODO Auto-generated method stub
      locationClient.requestLocationUpdates(REQUEST, MainActivity.this);
   }

   @Override
   public void onDisconnected() {
      // TODO Auto-generated method stub

   }
   
   @Override
   public void onLocationChanged(Location location) {
      // TODO Auto-generated method stub
      if (!locationChanged) {
         myLocation = location;
         locationChanged = true;
         map.animateCamera(CameraUpdateFactory.newLatLngZoom(
                 new LatLng(myLocation.getLatitude(), myLocation.getLongitude()), zoomNormal), 800, null);
         if (progressbar != null) {
            // progressbar.setVisibility(View.GONE);
            new GetStroeInfoTask().execute();
         }
      }

      if (myLocation.distanceTo(location) > 50) {
         myLocation = location;
         new GetStroeInfoTask().execute();
      }

   }

   GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {

      @Override
      public boolean onMarkerClick(Marker marker) {
         Log.i("myTag", "onMmarkerClick");
         if(marker.getTitle().equals("myLocation"))
            return true;
         if (marker.getTitle().equals("store")) {
            // animationHandler.post(animationRunnable);
            // sl_Animation.LoadViewAnimation(AnimationActionState.DRIVER_LOAD);
            MarkerSelect(marker);
            return true;
         } else {
            return false;
         }
      }
   };
   private StoreInfo markerselect_item;

   private void MarkerSelect(Marker marker) {
         String snippet = marker.getSnippet();
         int storeListNumber = Integer.parseInt(snippet);
         markerselect_item = all_list.get(storeListNumber);

         map.animateCamera(CameraUpdateFactory.newLatLngZoom(
               new LatLng(Double.parseDouble(markerselect_item.get_lat()),
                     Double.parseDouble(markerselect_item.get_lng())),
               map.getCameraPosition().zoom), 800, null);

         marker.showInfoWindow();
      

   }

   class GetStroeInfoTask extends
         AsyncTask<String, String, ArrayList<StoreInfo>> {

      @Override
      protected ArrayList<StoreInfo> doInBackground(String... params) {

         return getStoreInfoSoap(String.valueOf(myLocation.getLatitude()),
               String.valueOf(myLocation.getLongitude()), 100);
      }

      @Override
      protected void onPostExecute(ArrayList<StoreInfo> result) {
         super.onPostExecute(result);
         progressbar.setVisibility(View.GONE);
         map.clear();
         if (info[3].equals("0")) {
            map.addMarker(new MarkerOptions()
                  .position(
                        new LatLng(myLocation.getLatitude(), myLocation
                              .getLongitude()))
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.fork_man))
                  .title("myLocation"));
         } else {
            map.addMarker(new MarkerOptions()
                  .position(
                        new LatLng(myLocation.getLatitude(), myLocation
                              .getLongitude()))
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.fork_woman))
                  .title("myLocation"));
         }
         if (result.size() != 0) {

            all_list = result;

            for (int i = 0; i < result.size(); i++) {
               StoreInfo item = new StoreInfo();
               item = result.get(i);

               double lat = Double.parseDouble(item.get_lat().toString());
               double lng = Double.parseDouble(item.get_lng().toString());

               switch (Integer.parseInt(item.get_type())) {
               case 0:
                  map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .snippet(String.valueOf(i))
                        .title("store")
                        .icon(BitmapDescriptorFactory
                              .fromResource(R.drawable.fork_kor)));
                  break;
               case 1:
                  map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .snippet(String.valueOf(i))
                        .title("store")
                        .icon(BitmapDescriptorFactory
                              .fromResource(R.drawable.fork_chinese)));
                  break;
               case 2:
                  map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .snippet(String.valueOf(i))
                        .title("store")
                        .icon(BitmapDescriptorFactory
                              .fromResource(R.drawable.fork_japan)));
                  break;
               case 3:
                  map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .snippet(String.valueOf(i))
                        .title("store")
                        .icon(BitmapDescriptorFactory
                              .fromResource(R.drawable.fork_eng)));
                  break;
               case 4:
                  map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .snippet(String.valueOf(i))
                        .title("store")
                        .icon(BitmapDescriptorFactory
                              .fromResource(R.drawable.fork_etc)));
                  break;
               case 5:
                  map.addMarker(new MarkerOptions()
                        .position(new LatLng(lat, lng))
                        .snippet(String.valueOf(i))
                        .title("store")
                        .icon(BitmapDescriptorFactory
                              .fromResource(R.drawable.mysetlocation)));
                  break;
               }

               // SimpleDateFormat dateFormat = new SimpleDateFormat(
               // "yyyy/MM/dd hh:mm:ss", Locale.getDefault()); Date date =
               // new Date(); Date nowDate = new Date(); try { date =
               // dateFormat.parse(item.get("regdate").toString()); } catch
               // (Exception e) { }
               //
               // long diffInSec = TimeUnit.MILLISECONDS.toSeconds(nowDate
               // .getTime() - date.getTime());
               //
               // // if (diffInSec <= 10) {
               // System.out.println("fgn ALIVE driver = " +
               // item.get("driver_idx").toString() + "  diff = " +
               // diffInSec);
               //
               // double newLat = Double.parseDouble(item.get("latitude")
               // .toString()); double newLng =
               // Double.parseDouble(item.get("longitude") .toString());
               //
               // double oldLat =
               // Double.parseDouble(item.get("old_latitude") .toString());
               // double oldLng = Double.parseDouble(item
               // .get("old_longitude").toString());
               //
               // String[] strings = { item.get("state").toString(),
               // item.get("fcount").toString(),
               // item.get("dlocation").toString(),
               // item.get("dsosok").toString(),
               // item.get("dcartype").toString(),
               // item.get("faveFlag").toString() };
               //
               // String snippet = item.get("driver_idx").toString() + "|"
               // item.get("dlocation").toString() + "|" +
               // item.get("dcartype").toString() + "|" +
               // item.get("dcarname").toString() + "|" +
               // item.get("dcarnum").toString() + "|" +
               // item.get("dpaytype").toString() + "|" + i;
               //
               // if (oldLat == newLat) { map.addMarker(new MarkerOptions()
               // .position(new LatLng(newLat, newLng))
               // .title("driver").snippet(snippet)
               // .icon(getBitmapDescriptor(strings))); } else {
               // animationUtil.animateMarkerToGB( map.addMarker(new
               // MarkerOptions() .position(new LatLng(oldLat, oldLng))
               // .title("driver").snippet(snippet)
               // .icon(getBitmapDescriptor(strings))), new LatLng(newLat,
               // newLng), new Spherical());
               // }

            }
         }
      }

   }

   private ArrayList<StoreInfo> getStoreInfoSoap(String lat, String lng,
         int distance_set) {
      ArrayList<StoreInfo> storeList = new ArrayList<StoreInfo>();

      try {
         SoapObject request = new SoapObject(WebServiceUtil.NAMESPACE,
               WebServiceUtil.tasty_store_info_select_method_name);
         Log.d("MyTag", "lat = " + lat);
         Log.d("MyTag", "lng = " + lng);
         request.addProperty("lat", lat);
         request.addProperty("lng", lng);
         request.addProperty("distance_set", distance_set);

         SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
               SoapEnvelope.VER11);
         envelope.dotNet = true;
         envelope.encodingStyle = SoapSerializationEnvelope.XSD;
         envelope.setOutputSoapObject(request);

         HttpTransportSE androidHttpTransport = new HttpTransportSE(
               WebServiceUtil.URL);
         androidHttpTransport.call(
               WebServiceUtil.store_info_select_soap_action, envelope);

         SoapObject table = (SoapObject) envelope.getResponse();
         storeList.clear();

         for (int i = 0; i < table.getPropertyCount(); i++) {
            SoapObject element = (SoapObject) table.getProperty(i);

            StoreInfo item = new StoreInfo();

            item.put("ipx", element.getProperty("ipx").toString());

            item.put("name", element.getProperty("name").toString());
            item.put("ceo", element.getProperty("ceo").toString());
            item.put("type", element.getProperty("type").toString());
            item.put("tel", element.getProperty("tel1").toString() + "-"
                  + element.getProperty("tel2").toString() + "-"
                  + element.getProperty("tel3").toString());

            item.put("detail", element.getProperty("detail").toString());
            item.put("address", element.getProperty("address").toString());
            item.put("review_count", element.getProperty("review_count")
                  .toString());
            item.put("rate_count", element.getProperty("rate_count")
                  .toString());
            item.put("rate", element.getProperty("rate").toString());
            item.put("lat", element.getProperty("lat").toString());
            item.put("lng", element.getProperty("lng").toString());
            item.put("distance", element.getProperty("distance1")
                  .toString());
            item.put("rate_count", element.getProperty("rate_count")
                  .toString());
            item.put("favorite_count",
                  element.getProperty("favorites_count").toString());

            storeList.add(item);
         }

      } catch (Exception e) {
         storeList.clear();
         Log.e("MyTag", "getCurrentDriversSoap Error : " + e.toString());
      }

      return storeList;
   }

   @Override
   public void onCallBackActivity() {
      // TODO Auto-generated method stub
      Log.d("MyTag", "enter onresume");
      if (filter_dialog_call) {
         filter_flag = DialogFilter.get_flag();
         filter_dialog_call = false;
         Log.d("MyTag", "filter_flag = " + filter_flag[0]);
         refreshMarker(filter_flag);

      }
   }

   private void refreshMarker(String[] filter_flag) {
      map.clear();

      if (info[3].equals("0")) {
         map.addMarker(new MarkerOptions()
               .position(
                     new LatLng(myLocation.getLatitude(), myLocation
                           .getLongitude()))
               .icon(BitmapDescriptorFactory
                     .fromResource(R.drawable.fork_man))
               .title("myLocation"));
      } else {
         map.addMarker(new MarkerOptions()
               .position(
                     new LatLng(myLocation.getLatitude(), myLocation
                           .getLongitude()))
               .icon(BitmapDescriptorFactory
                     .fromResource(R.drawable.fork_woman))
               .title("myLocation"));
      }

      for (int i = 0; i < all_list.size(); i++) {
         StoreInfo item = new StoreInfo();
         item = all_list.get(i);

         if (filter_flag[0].equals(item.get_type())) {
            double lat = Double.parseDouble(item.get_lat().toString());
            double lng = Double.parseDouble(item.get_lng().toString());

            map.addMarker(new MarkerOptions()
                  .position(new LatLng(lat, lng))
                  .snippet(String.valueOf(i))
                  .title("store")
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.fork_kor)));
         } else if (filter_flag[1].equals(item.get_type())) {

            double lat = Double.parseDouble(item.get_lat().toString());
            double lng = Double.parseDouble(item.get_lng().toString());

            map.addMarker(new MarkerOptions()
                  .position(new LatLng(lat, lng))
                  .snippet(String.valueOf(i))
                  .title("store")
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.fork_chinese)));
         } else if (filter_flag[2].equals(item.get_type())) {
            double lat = Double.parseDouble(item.get_lat().toString());
            double lng = Double.parseDouble(item.get_lng().toString());

            map.addMarker(new MarkerOptions()
                  .position(new LatLng(lat, lng))
                  .snippet(String.valueOf(i))
                  .title("store")
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.fork_japan)));
         }

         else if (filter_flag[3].equals(item.get_type())) {
            double lat = Double.parseDouble(item.get_lat().toString());
            double lng = Double.parseDouble(item.get_lng().toString());

            map.addMarker(new MarkerOptions()
                  .position(new LatLng(lat, lng))
                  .snippet(String.valueOf(i))
                  .title("store")
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.fork_eng)));
         } else if (filter_flag[4].equals(item.get_type())) {
            double lat = Double.parseDouble(item.get_lat().toString());
            double lng = Double.parseDouble(item.get_lng().toString());

            map.addMarker(new MarkerOptions()
                  .position(new LatLng(lat, lng))
                  .snippet(String.valueOf(i))
                  .title("store")
                  .icon(BitmapDescriptorFactory
                        .fromResource(R.drawable.fork_etc)));
         }

      }

   }

   int count = 0;

   public boolean onKeyDown(int keyCode, KeyEvent event) {
      // TODO Auto-generated method stub
      switch (keyCode) {
      case KeyEvent.KEYCODE_BACK:
         if (count == 0) {
            Toast.makeText(this, "뒤로가기를 한번 더 누르시면 종료됩니다.",
                  Toast.LENGTH_SHORT).show();
            exitHandler.sendEmptyMessageDelayed(0, 2000);
            count++;

         } else if (count == 1) {
            act.Allremove(this);
            finish();
            break;
         }

      }
      return false;
   }

   Handler exitHandler = new Handler(new Handler.Callback() {

      @Override
      public boolean handleMessage(Message msg) {
         if (msg.what == 0) {
            count = 0;
         }
         return false;
      }
   });

}