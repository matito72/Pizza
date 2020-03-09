package com.example.pizza;

import android.Manifest;
import android.accounts.AccountManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.flatdialoglibrary.dialog.FlatDialog;
import com.example.pizza.bean.ItemBean;
import com.example.pizza.listPizza.ListPizza;
import com.example.pizza.listPizza.ListPizzaActivity;
import com.example.pizza.listPizza.PizzaBean;
import com.example.pizza.listSnack.ListSnackActivity;
import com.example.pizza.listSnack.SnackBean;
import com.example.pizza.ui.gallery.GalleryFragment;
import com.example.pizza.ui.home.HomeFragment;
import com.example.pizza.ui.send.SendFragment;
import com.example.pizza.ui.settings.SettingsFragment;
import com.example.pizza.ui.share.ShareFragment;
import com.example.pizza.ui.slideshow.SlideshowFragment;
import com.example.pizza.ui.tools.ToolsFragment;
import com.example.pizza.util.DrawableProvider;
import com.example.pizza.util.Util;
import com.github.clans.fab.FloatingActionMenu;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.Scopes;
import com.google.android.material.navigation.NavigationView;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GooglePlayServicesAvailabilityIOException;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.sheets.v4.Sheets;
import com.google.api.services.sheets.v4.SheetsScopes;
import com.google.api.services.sheets.v4.model.BandedRange;
import com.google.api.services.sheets.v4.model.BandingProperties;
import com.google.api.services.sheets.v4.model.BatchGetValuesByDataFilterRequest;
import com.google.api.services.sheets.v4.model.BatchGetValuesByDataFilterResponse;
import com.google.api.services.sheets.v4.model.BatchUpdateSpreadsheetRequest;
import com.google.api.services.sheets.v4.model.CellData;
import com.google.api.services.sheets.v4.model.Color;
import com.google.api.services.sheets.v4.model.DataFilter;
import com.google.api.services.sheets.v4.model.ExtendedValue;
import com.google.api.services.sheets.v4.model.GetSpreadsheetByDataFilterRequest;
import com.google.api.services.sheets.v4.model.GridCoordinate;
import com.google.api.services.sheets.v4.model.Request;
import com.google.api.services.sheets.v4.model.RowData;
import com.google.api.services.sheets.v4.model.Sheet;
import com.google.api.services.sheets.v4.model.Spreadsheet;
import com.google.api.services.sheets.v4.model.UpdateCellsRequest;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

//
//import com.example.flatdialoglibrary.dialog.FlatDialog;
//import com.example.pizza.ui.settings.SettingsFragment;
//import com.example.pizza.ui.tools.ToolsFragment;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {

    private AppBarConfiguration mAppBarConfiguration;

    GoogleAccountCredential mCredential;

    static final int REQUEST_ACCOUNT_PICKER = 1000;
    static final int REQUEST_AUTHORIZATION = 1001;
    static final int REQUEST_GOOGLE_PLAY_SERVICES = 1002;
    static final int REQUEST_PERMISSION_GET_ACCOUNTS = 1003;

    public static final int SELECT_PIZZA = 30;
    public static final int SELECT_NOTE = 50;
    public static final int SELECT_SNACK = 40;

//    private static final String BUTTON_TEXT = "Call Google Sheets API";
    private static final String PREF_ACCOUNT_NAME = "accountName";
    private static final String[] SCOPES = { SheetsScopes.SPREADSHEETS };

    private String nome;
    private String cognome;
    private List<PizzaBean> mDataList;

    private PizzaBean pizzaBeanSel;
    private ItemBean itemBeanPizzaSel;

    private SnackBean snackBeanSel;
    private ItemBean itemBeanSnackSel;

    private Boolean trasportoDaPagareSel;
    private Boolean pagatoSel;
    private Boolean pizzaBabySel;

    private String noteSel;
    private ItemBean itemBeanNoteSel;

    private int posNomeCognome;
    private String dataUltimoPagamentoTrasporto;

    private SparseArray<Fragment> mapFragment;
    private com.google.api.services.sheets.v4.Sheets mService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.action_settings,
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow,
                R.id.nav_tools, R.id.nav_share, R.id.nav_send, R.id.nav_settings)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        FloatingActionMenu menu = findViewById(R.id.menu);
        com.github.clans.fab.FloatingActionButton btnPizze = findViewById(R.id.btnPizze);
        btnPizze.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListPizzaActivity.class) ;
                intent.putExtra(Constant.TYPE, DrawableProvider.SAMPLE_ROUND);
                intent.putExtra(Constant.LIST_PIZZA, new ListPizza(MainActivity.this.mDataList));
                startActivityForResult(intent, SELECT_PIZZA);

                menu.close(false);
            }
        });

        com.github.clans.fab.FloatingActionButton btnSnack = findViewById(R.id.btnSnack);
        btnSnack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListSnackActivity.class) ;
                startActivityForResult(intent, SELECT_SNACK);

                menu.close(false);
            }
        });

        com.github.clans.fab.FloatingActionButton btnNote = findViewById(R.id.btnNote);
        btnNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.this.openPopupNoteAndBabyPizza(view);

                menu.close(false);
            }
        });

        // Initialize credentials and service object.
        mCredential = GoogleAccountCredential.usingOAuth2(
                getApplicationContext(), Arrays.asList(SCOPES))
                .setBackOff(new ExponentialBackOff());
    }

    public List<PizzaBean> getDataList() {
        return this.mDataList;
    }

    public void setPizzaBabySel(Boolean isPizzaBaby) {
        this.pizzaBabySel = isPizzaBaby;
    }

    /**
     * popup NOTE e BABY PIZZA
     */
    public void openPopupNoteAndBabyPizza(View view) {
//        final String[] items = {"Pizza Baby"};
//        final boolean[] ckItems = new boolean[]{(this.pizzaBabySel != null && this.pizzaBabySel)};
        final ArrayList<Integer> selectedList = new ArrayList<Integer>();

        final EditText input = new EditText(this);
        input.setHint("Note:");
        if (this.noteSel != null) {
            input.setText(this.noteSel);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("    ");
//        builder.setMultiChoiceItems(items, ckItems,
//                new DialogInterface.OnMultiChoiceClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
//                        if (isChecked) {
//                            selectedList.add(which);
//                        } else if (selectedList.contains(which)) {
//                            selectedList.remove(Integer.valueOf(which));
//                        }
//                    }
//                });

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        builder.setView(input);


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // ...
            }
        });

        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
//                ArrayList selectedStrings = new ArrayList<>();
//                for (int j = 0; j < selectedList.size(); j++) {
//                    selectedStrings.add(items[selectedList.get(j)]);
//                }

//                MainActivity.this.pizzaBabySel = selectedStrings.size() != 0;
                MainActivity.this.noteSel = input.getText().toString();

                HomeFragment homeFragment = (HomeFragment) mapFragment.get(R.id.nav_home);
                LiveData<List<ItemBean>> dataItemBean = homeFragment.getHomeViewModel().getDataItemBean();
                for (ItemBean item : dataItemBean.getValue()) {
                    item.setPizzaBaby(MainActivity.this.pizzaBabySel);
                    item.setNote(MainActivity.this.noteSel);
                }

                new MakeRequestTask(mCredential, false).execute();
            }
        });

        builder.show();
    }

    public void execMakRequestTask() {
        new MakeRequestTask(mCredential, false).execute();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

//    public void onSettingsClick(View view) {
//        Log.i(Constant.TAG, "Test");
//    }

    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(Constant.TAG, "Test");
        aggiornaMapFragment();

        switch (item.getItemId()) {
            case R.id.action_settings:
                final FlatDialog flatDialog = new FlatDialog(MainActivity.this);

                flatDialog.setTitle("Login")
                        .setSubtitle("write your profile info here")
                        .setFirstTextField(this.nome)
                        .setSecondTextField(this.cognome)
                        .setFirstTextFieldHint("Nome")
                        .setSecondTextFieldHint("Cognome")
                        .setFirstButtonText("CONNECT")
                        .setSecondButtonText("CANCEL")
                        .withFirstButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                MainActivity.this.nome = flatDialog.getFirstTextField();
                                MainActivity.this.cognome = flatDialog.getSecondTextField();

                                if (MainActivity.this.nome != null && MainActivity.this.nome.trim().length() != 0
                                        && MainActivity.this.cognome != null && MainActivity.this.cognome.trim().length() != 0) {
                                    SharedPreferences.Editor editor = getSharedPreferences(Constant.PIZZA_PREFS, MODE_PRIVATE).edit();
                                    editor.putString("nomeDef", MainActivity.this.nome);
                                    editor.putString("cognomeDef", MainActivity.this.cognome);
                                    editor.apply();

                                    // INIT variabili:
                                    MainActivity.this.initVariabili();

                                    MainActivity.this.getResultsFromApi(false);
                                    flatDialog.dismiss();
                                } else {
                                    Toast.makeText(MainActivity.this, "Nome e/o Cognome non validi.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        })
                        .withSecondButtonListner(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                flatDialog.dismiss();
                            }
                        }).show();

        }
        return super.onOptionsItemSelected(item);
    }

    public void initVariabili() {
        mDataList = null;
        pizzaBeanSel = null;
        pizzaBabySel = null;
        pagatoSel = null;
        noteSel = null;
        posNomeCognome = 0;
        itemBeanPizzaSel = null;
        snackBeanSel = null;
        itemBeanSnackSel = null;
    }

    public boolean testInit() {
        return this.itemBeanNoteSel != null;
    }

    /**
     * Attempt to call the API, after verifying that all the preconditions are
     * satisfied. The preconditions are: Google Play Services installed, an
     * account was selected and the device currently has online access. If any
     * of the preconditions are not satisfied, the app will prompt the user as
     * appropriate.
     */
    public void getResultsFromApi(boolean reload) {
        if (!isGooglePlayServicesAvailable()) {
            acquireGooglePlayServices();
        } else if (mCredential.getSelectedAccountName() == null) {
            chooseAccount();
        } else if (! isDeviceOnline()) {
//            mOutputText.setText("No network connection available.");
        } else {
            SmoothProgressBar smoothProgressBar = findViewById(R.id.progressbar);
            if (smoothProgressBar != null) {
                smoothProgressBar.setVisibility(View.VISIBLE);
                smoothProgressBar.progressiveStart();
            }
            new MakeRequestTask(mCredential, reload).execute();
        }
    }

//    public void reload() {
//        new MakeRequestTask(mCredential).execute();
//    }

    @Override
    protected void onStart() {
        super.onStart();

        SharedPreferences prefs = getSharedPreferences(Constant.PIZZA_PREFS, MODE_PRIVATE);
        String nomeDef = prefs.getString("nomeDef", "");
        String cognomeDef = prefs.getString("cognomeDef", "");
        if (!"".equals(nomeDef) && !"".equals(cognomeDef)) {
            this.nome = nomeDef;
            this.cognome = cognomeDef;

            this.aggiornaMapFragment();
            this.getResultsFromApi(false);
        } else {
            getSupportActionBar().setTitle("Utente non loggato!");

            SmoothProgressBar smoothProgressBar = findViewById(R.id.progressbar);
            smoothProgressBar.progressiveStop();
        }
    }



    public void aggiornaMapFragment() {
        if (mapFragment == null)
            mapFragment = new SparseArray<Fragment>();

        List<Fragment> lstFragment = getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment).getChildFragmentManager().getFragments();
        if (lstFragment.size() != 0 && lstFragment.get(0) != null) {
            Fragment fragment = lstFragment.get(0);

            if (fragment instanceof GalleryFragment && mapFragment.get(R.id.nav_gallery) == null) {
                mapFragment.put(R.id.nav_gallery, fragment);
            } else if (fragment instanceof HomeFragment) { // && mapFragment.get(R.id.nav_home) == null) {
                mapFragment.put(R.id.nav_home, fragment);
            } else if (fragment instanceof SendFragment && mapFragment.get(R.id.nav_send) == null) {
                mapFragment.put(R.id.nav_send, fragment);
            } if (fragment instanceof SettingsFragment && mapFragment.get(R.id.nav_settings) == null) {
                mapFragment.put(R.id.nav_settings, fragment);
            } else if (fragment instanceof ShareFragment && mapFragment.get(R.id.nav_share) == null) {
                mapFragment.put(R.id.nav_share, fragment);
            } else if (fragment instanceof SlideshowFragment && mapFragment.get(R.id.nav_slideshow) == null) {
                mapFragment.put(R.id.nav_slideshow, fragment);
            } else if (fragment instanceof ToolsFragment && mapFragment.get(R.id.nav_tools) == null) {
                mapFragment.put(R.id.nav_tools, fragment);
            }
        }

        ImageView imgPizza = findViewById(R.id.imgPizza);
        if (imgPizza != null) {
            imgPizza.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://it.wikipedia.org/wiki/Teorema_della_pizza"));
                    startActivity(browserIntent);
                }
            });
        }

        TextView txtEmail = findViewById(R.id.email);
        if (txtEmail != null) {
            txtEmail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String mailTo = getString(R.string.nav_header_subtitle);

                    Intent intentMail = new Intent(Intent.ACTION_SEND);
                    intentMail.setType("message/rfc822");
                    intentMail.putExtra(Intent.EXTRA_EMAIL, new String[]{ mailTo });
                    intentMail.putExtra(Intent.EXTRA_SUBJECT, "PiZZA!");
                    intentMail.putExtra(Intent.EXTRA_TEXT, "..con i colleghi");

                    try {
                        startActivity(Intent.createChooser(intentMail, "GMAIL-PiZZA!"));
                    } finally {

                    }
                }
            });
        }
    }

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
    }

    /**
     * Attempts to set the account used with the API credentials. If an account
     * name was previously saved it will use that one; otherwise an account
     * picker dialog will be shown to the user. Note that the setting the
     * account to use with the credentials object requires the app to have the
     * GET_ACCOUNTS permission, which is requested here if it is not already
     * present. The AfterPermissionGranted annotation indicates that this
     * function will be rerun automatically whenever the GET_ACCOUNTS permission
     * is granted.
     */
    @AfterPermissionGranted(REQUEST_PERMISSION_GET_ACCOUNTS)
    private void chooseAccount() {
        if (EasyPermissions.hasPermissions(
                this, Manifest.permission.GET_ACCOUNTS)) {
            String accountName = getPreferences(Context.MODE_PRIVATE)
                    .getString(PREF_ACCOUNT_NAME, null);
            if (accountName != null) {
                mCredential.setSelectedAccountName(accountName);
                getResultsFromApi(false);
            } else {
                // Start a dialog from which the user can choose an account
                startActivityForResult(
                        mCredential.newChooseAccountIntent(),
                        REQUEST_ACCOUNT_PICKER);
            }
        } else {
            // Request the GET_ACCOUNTS permission via a user dialog
            EasyPermissions.requestPermissions(
                    this,
                    "This app needs to access your Google account (via Contacts).",
                    REQUEST_PERMISSION_GET_ACCOUNTS,
                    Manifest.permission.GET_ACCOUNTS);
        }
    }

    /**
     * Called when an activity launched here (specifically, AccountPicker
     * and authorization) exits, giving you the requestCode you started it with,
     * the resultCode it returned, and any additional data from it.
     * @param requestCode code indicating which activity result is incoming.
     * @param resultCode code indicating the result of the incoming
     *     activity result.
     * @param data Intent (containing result data) returned by incoming
     *     activity result.
     */
    @Override
    protected void onActivityResult(
            int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case REQUEST_GOOGLE_PLAY_SERVICES:
                if (resultCode != RESULT_OK) {
                    int x = 0;

//                    mOutputText.setText(
//                            "This app requires Google Play Services. Please install " +
//                                    "Google Play Services on your device and relaunch this app.");
                } else {
                    getResultsFromApi(false);
                }
                break;
            case REQUEST_ACCOUNT_PICKER:
                if (resultCode == RESULT_OK && data != null &&
                        data.getExtras() != null) {
                    String accountName =
                            data.getStringExtra(AccountManager.KEY_ACCOUNT_NAME);
                    if (accountName != null) {
                        SharedPreferences settings =
                                getPreferences(Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString(PREF_ACCOUNT_NAME, accountName);
                        editor.apply();
                        mCredential.setSelectedAccountName(accountName);
                        getResultsFromApi(false);
                    }
                }
                break;
            case REQUEST_AUTHORIZATION:
                if (resultCode == RESULT_OK) {

                    getResultsFromApi(false);
                }
                break;
            case SELECT_PIZZA:
                if (data != null && data.getExtras() != null) {
                    this.pizzaBeanSel = (PizzaBean) data.getExtras().get(Constant.PIZZA_SEL);
                    if (this.pizzaBeanSel != null)
                        this.pizzaBeanSel.setTrasporto(this.dataUltimoPagamentoTrasporto.equals(Util.getStrDataAttuale()));
                }
                break;
            case SELECT_SNACK:
                if (data != null && data.getExtras() != null) {
                    this.snackBeanSel = (SnackBean) data.getExtras().get(Constant.SNACK_SEL);
                    if (this.snackBeanSel != null) {
//                        findViewById(R.id.snackSel).setVisibility(View.VISIBLE);
//
//                        ((ImageView)findViewById(R.id.snackIdImmagine)).setImageResource(this.snackBeanSel.getIdImmagine());
//                        ((ImageView)findViewById(R.id.iconRemoveSnack)).setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                findViewById(R.id.snackSel).setVisibility(View.GONE);
//                            }
//                        });
//                        ((TextView)findViewById(R.id.snackTitolo)).setText(this.snackBeanSel.getTitolo());
//                        ((TextView)findViewById(R.id.snackDescrizione)).setText(this.snackBeanSel.getDescrizione() + "\r\n" + this.snackBeanSel.getDescrizione2() + " " + this.snackBeanSel.getStrEuro());

                        MainActivity.this.itemBeanSnackSel = new ItemBean(snackBeanSel.getTitolo(), snackBeanSel.getDescrizione(),
                                snackBeanSel.getDescrizione2(), snackBeanSel.getStrEuro(), this.snackBeanSel.getIdImmagine(), 2);

//                        lstItemBean.add(itemBeanPizzaSel);
                        HomeFragment homeFragment = (HomeFragment) mapFragment.get(R.id.nav_home);
                        homeFragment.getHomeViewModel().addItemBean(MainActivity.this.itemBeanSnackSel);
//                        this.lstItemBean = homeFragment.getHomeViewModel().addItemBean(MainActivity.this.itemBeanSnackSel);
//                        homeFragment.getHomeViewModel().changeDataItemBeanSel(homeFragment.getHomeViewModel().getDataItemBean().getValue());
                    }
                }
                break;
        }
    }


    /**
     * Respond to requests for permissions at runtime for API 23 and above.
     * @param requestCode The request code passed in
     *     requestPermissions(android.app.Activity, String, int, String[])
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either PERMISSION_GRANTED or PERMISSION_DENIED. Never null.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(
                requestCode, permissions, grantResults, this);
    }

    /**
     * Callback for when a permission is granted using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Callback for when a permission is denied using the EasyPermissions
     * library.
     * @param requestCode The request code associated with the requested
     *         permission
     * @param list The requested permission list. Never null.
     */
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Do nothing.
    }

    /**
     * Checks whether the device currently has a network connection.
     * @return true if the device has a network connection, false otherwise.
     */
    private boolean isDeviceOnline() {
        ConnectivityManager connMgr =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnected());
    }

    /**
     * Check that Google Play services APK is installed and up to date.
     * @return true if Google Play Services is available and up to
     *     date on this device; false otherwise.
     */
    private boolean isGooglePlayServicesAvailable() {
        GoogleApiAvailability apiAvailability =
                GoogleApiAvailability.getInstance();
        final int connectionStatusCode =
                apiAvailability.isGooglePlayServicesAvailable(this);
        return connectionStatusCode == ConnectionResult.SUCCESS;
    }

    /**
     * Attempt to resolve a missing, out-of-date, invalid or disabled Google
     * Play Services installation via a user dialog, if possible.
     */
    private void acquireGooglePlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        final int connectionStatusCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (apiAvailability.isUserResolvableError(connectionStatusCode)) {
            showGooglePlayServicesAvailabilityErrorDialog(connectionStatusCode);
        }
    }


    /**
     * Display an error dialog showing that Google Play Services is missing
     * or out of date.
     * @param connectionStatusCode code describing the presence (or lack of)
     *     Google Play Services on this device.
     */
    void showGooglePlayServicesAvailabilityErrorDialog(
            final int connectionStatusCode) {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        Dialog dialog = apiAvailability.getErrorDialog(
                MainActivity.this,
                connectionStatusCode,
                REQUEST_GOOGLE_PLAY_SERVICES);
        dialog.show();
    }

    public PizzaBean getPizzaBean() {
        return pizzaBeanSel;
    }

    /**
     * An asynchronous task that handles the Google Sheets API call.
     * Placing the API calls in their own task ensures the UI stays responsive.
     */
    private class MakeRequestTask extends AsyncTask<Void, Void, Object[]> {
        private Exception mLastError = null;
        private boolean reload;

        MakeRequestTask(GoogleAccountCredential credential, boolean reload) {
            this.reload = reload;
            HttpTransport transport = AndroidHttp.newCompatibleTransport();
            JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
            mService = new com.google.api.services.sheets.v4.Sheets.Builder(
                    transport, jsonFactory, credential)
                    .setApplicationName("Foglio PiZZA !!!")
                    .build();
        }

        /**
         * Background task to call Google Sheets API.
         * @param params no parameters needed for this task.
         */
        @Override
        protected Object[] doInBackground(Void... params) {
            try {
                Object[] arr2Obj =  getDataFromApi();
                List<PizzaBean> lstPizzaBean = (List<PizzaBean>) arr2Obj[0];

//                if (arr2Obj != null && arr2Obj.length >= 2 && arr2Obj[1] != null) {
//                    List<ItemBean> lstItemBean = (List<ItemBean>) arr2Obj[1];
//                    if (this.reload) {
//                        HomeFragment homeFragment = (HomeFragment) mapFragment.get(R.id.nav_home);
//                        homeFragment.aggiornaData(lstItemBean);
//                    }
//                }

                return arr2Obj;
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(Constant.TAG, e.getMessage());
                mLastError = e;
                cancel(true);
                return null;
            }
        }

        @Override
        protected void onCancelled(Object[] arr2Obj) {
            super.onCancelled(arr2Obj);
        }

        private void testUno() {
            String spreadsheetId =Constant.ID_SHEET_PIZZA; // TODO: Update placeholder value.

            // The DataFilters used to select which ranges to retrieve from the spreadsheet.
            DataFilter filter = new DataFilter();
            filter.set("a1Range", "Ordine!A4:A27");
            List<DataFilter> dataFilters = new ArrayList<>(); // TODO: Update placeholder value.
            dataFilters.add(filter);

            // TODO: Assign values to desired fields of `requestBody`:
            GetSpreadsheetByDataFilterRequest requestBody = new GetSpreadsheetByDataFilterRequest();
            requestBody.setDataFilters(dataFilters);
            requestBody.setIncludeGridData(true);

            try {
                Spreadsheet response = mService.spreadsheets().getByDataFilter(spreadsheetId, requestBody).execute();

                int numNominativi = response.getSheets().get(0).getData().get(0).getRowData().size();
                String nomeCognome = response.getSheets().get(0).getData().get(0).getRowData().get(0).getValues().get(0).getFormattedValue();
                Color color = response.getSheets().get(0).getData().get(0).getRowData().get(0).getValues().get(0).getEffectiveFormat().getBackgroundColor();
                String colorGrigioScuro = "{\"blue\":0.9529412,\"green\":0.9529412,\"red\":0.9529412}";
                String colorGigioChiaro = "{\"blue\":1.0,\"green\":1.0,\"red\":1.0}";

//                String nomeCognome_i = response.getSheets().get(0).getData().get(0).getRowData().get(i).getValues().get(0).getFormattedValue();
//                Color backgroundColor_i = response.getSheets().get(0).getData().get(0).getRowData().get(i).getValues().get(0).getEffectiveFormat().getBackgroundColor();

                Log.i(Constant.TAG, "");
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(Constant.TAG, e.getMessage());
            }
        }

        private void testDue() {
            String spreadsheetId = Constant.ID_SHEET_PIZZA;
            List<String> ranges = new ArrayList<>();
            boolean includeGridData = false; // TODO: Update placeholder value.

            try {
                Sheets sheetsService = mService;
                Sheets.Spreadsheets.Get request = sheetsService.spreadsheets().get(spreadsheetId);
                request.setRanges(ranges);
                request.setIncludeGridData(includeGridData);
                Spreadsheet resp = request.execute();

                ArrayList<Sheet> lstSheet = (ArrayList)resp.get("sheets");
                if (lstSheet != null && lstSheet.size() != 0) {
                    int numSheet = 0;
                    for (Sheet sheet : lstSheet) {
                        Log.i(Constant.TAG, "Sheet." + numSheet++);

                        List<BandedRange> lstBandedRange = sheet.getBandedRanges();
                        if (lstBandedRange != null && lstBandedRange.size() != 0) {
                            int numBandedRange = 0;
                            for (BandedRange bandedRange : lstBandedRange) {
                                BandingProperties bandingPropertiesRow = bandedRange.getRowProperties();
                                BandingProperties bandingPropertiesCol = bandedRange.getColumnProperties();

                                Log.i(Constant.TAG, "bandedRange." + numBandedRange++);
                            }
                        }
                    }
                }

                // ((BandedRange)((ArrayList)((Sheet)((ArrayList)resp.get("sheets")).get(0)).getBandedRanges()).get(1)).getRowProperties()

                Log.i(Constant.TAG, resp.toString());
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(Constant.TAG, e.getMessage());
            }
        }

        /**
         * Fetch a list of names and majors of students in a sample spreadsheet:
         * https://docs.google.com/spreadsheets/d/1BxiMVs0XRA5nFMdKvBdBZjgmUUqptlbs74OgvE2upms/edit
         * @return List of names and majors
         * @throws IOException
         */
        private Object[] getDataFromApi() throws Exception {
//            List<ItemBean> lstItemBean = new ArrayList<>();
//            ItemBean itemBeanRead = null;
            Object[] arr2Obj = new Object[2];
            ArrayList<ItemBean> lstItemBean = new ArrayList<ItemBean>();
            PizzaBean pizzaBeanRead = null;
            SnackBean snackBeanRead = null;
            boolean pizzaBabyRead = false;
            boolean trasportoDaPagareRead = false;
            boolean pagatoRead = false;
            String noteRead = null;
            SmoothProgressBar smoothProgressBar = findViewById(R.id.progressbar);

            if (MainActivity.this.mDataList == null) {
                // ------------------------------------------
                // testUno();
                // testDue();
                // ------------------------------------------

                ValueRange response = mService.spreadsheets().values().get(Constant.ID_SHEET_PIZZA, "Pizze!A24:Z").execute();
                List<List<Object>> lstPizze = response.getValues();

                MainActivity.this.mDataList = new ArrayList<PizzaBean>(lstPizze.size() - 1);
                for(List<Object> pizza : lstPizze) {
                    PizzaBean pizzaBean = new PizzaBean((String)pizza.get(4), (String)pizza.get(1), (String)pizza.get(2), false );
                    if (pizzaBean.getTitolo() != null && pizzaBean.getDescrizione2() != null && pizzaBean.getPrezzo().compareTo(BigDecimal.ZERO) > 0) {
                        MainActivity.this.mDataList.add(pizzaBean);
                    }
                }
            }

            if (MainActivity.this.posNomeCognome == 0) {
                // Recupera la lista dei Nome Cognome
                ValueRange response = mService.spreadsheets().values().get(Constant.ID_SHEET_PIZZA, "Ordine!A4:Z").execute();
                List<List<Object>> lstNomeCognome = response.getValues();

                if (lstNomeCognome != null) {
                    String nomeCognomeLogin = MainActivity.this.nome + " " + MainActivity.this.cognome;

                    int numRow = 0;
                    for (List row : lstNomeCognome) {
                        if (nomeCognomeLogin.equals(row.get(0))) {
                            // ---------------------------------------------------------
                            // ---------------- TROVATO il NOME COGNOME ----------------
                            // ---------------------------------------------------------
                            int numCellOk = (Constant.START_COL_NAME + numRow);

                            DataFilter filter = new DataFilter();
                            filter.set("a1Range", "Ordine!A" + numCellOk + ":A" + numCellOk);
                            List<DataFilter> dataFilters = new ArrayList<>();
                            dataFilters.add(filter);

                            GetSpreadsheetByDataFilterRequest requestBody = new GetSpreadsheetByDataFilterRequest();
                            requestBody.setDataFilters(dataFilters);
                            requestBody.setIncludeGridData(true);

                            Spreadsheet resp = mService.spreadsheets().getByDataFilter(Constant.ID_SHEET_PIZZA, requestBody).execute();
                            List<RowData> lstRowData = resp.getSheets().get(0).getData().get(0).getRowData();
                            Color color = resp.getSheets().get(0).getData().get(0).getRowData().get(0).getValues().get(0).getEffectiveFormat().getBackgroundColor();
                            if (Constant.COLORE_TRASPORTO.equals(color.toString())) {
                                // ---------------- TRASPORTO DA PAGARE ----------------
                                trasportoDaPagareRead = true;
                            }
                            // ----------------------------------------------------------------------------------------------


                            MainActivity.this.dataUltimoPagamentoTrasporto = (String)row.get(13); // 04/12/2019
                            String dataAttuale = Util.getStrDataAttuale();
                            MainActivity.this.posNomeCognome = numRow + 3;

                            // ---------------------------------------------------
                            // ---------------- PAGATO ----------------
                            // ---------------------------------------------------
                            String strPagato = (String)row.get(6);
                            if ("TRUE".equals(strPagato)) {
                                pagatoRead = true;
                            }

                            // ---------------------------------------------------
                            // ---------------- PIZZA BABY ----------------
                            // ---------------------------------------------------
                            String strPizzaBaby = (String)row.get(2);
                            if ("TRUE".equals(strPizzaBaby)) {
                                pizzaBabyRead = true;
                            }

                            // ---------------------------------------------------
                            // ---------------- NOTE ----------------
                            // ---------------------------------------------------
                            String strNote = (String)row.get(4);
                            if (strNote != null && strNote.trim().length() != 0) {
                                noteRead = strNote;
                            }

                            // ---------------------------------------------------
                            // ---------------- PIZZA SELEZIONATA ----------------
                            // ---------------------------------------------------
                            String descrizionePizza = (String)row.get(1);
                            if (descrizionePizza != null && descrizionePizza.contains(": ") && descrizionePizza.contains(" (")) {
                                String titolo = descrizionePizza.substring(0, descrizionePizza.indexOf(": "));
                                String ingredienti = descrizionePizza.substring(titolo.length() + 2, descrizionePizza.lastIndexOf(" ("));
                                String strEuro = descrizionePizza.substring(titolo.length() + 2 + ingredienti.length() + 1 + 3, descrizionePizza.length() - 1);

                                pizzaBeanRead = new PizzaBean(titolo, ingredienti, strEuro, true);
                                pizzaBeanRead.setTrasporto(dataAttuale.equals(dataUltimoPagamentoTrasporto));

//                                itemBeanRead = new ItemBean(titolo, ingredienti, "", strEuro, R.drawable.pizza, 1);
                            } else {
                                pizzaBeanRead = new PizzaBean(Constant.NESSUNA_PIZZA_SELEZIONATA, "", "", true);
//                                itemBeanRead = new ItemBean(Constant.NESSUNA_PIZZA_SELEZIONATA, "", "", "", -1, 1);
                            }

                            // ---------------------------------------------------
                            // ---------------- SNACK SELEZIONATO ----------------
                            // ---------------------------------------------------
                            String descrizioneSnack = (String)row.get(3);
                            if (descrizioneSnack != null && descrizioneSnack.contains(" (")) {
                                String descrizione2 = descrizioneSnack.substring(0, descrizioneSnack.indexOf(" ("));
                                String titolo = descrizione2.replace("PORZIONE ", "");
                                String descrizione = descrizioneSnack.substring(descrizione2.length() + 1, descrizioneSnack.lastIndexOf(" ("));
                                String strEuro = descrizioneSnack.substring(descrizione2.length() + 1 + descrizione.length() + 1 + 3, descrizioneSnack.length() - 1);

                                snackBeanRead = new SnackBean(titolo, descrizione2.toLowerCase(), descrizione, strEuro, ListSnackActivity.findSnackBeanImageFromTitolo(titolo, descrizione2));

//                                itemBeanRead = new ItemBean(titolo, strNumPezzi, "", strEuro, R.drawable.pizza, 3);
                            } else {
                                snackBeanRead = new SnackBean(Constant.NESSUN_SNACK_SELEZIONATO, "", "", "", -1);
//                                itemBeanRead = new ItemBean(Constant.NESSUN_SNACK_SELEZIONATO, "", "", "", -1, 1);
                            }

                            break;
                        }
                        numRow++;
                    }
                }
            }

            if (mapFragment.get(R.id.nav_home) != null && (MainActivity.this.pizzaBeanSel != null || pizzaBeanRead != null)) {
                pizzaBeanRead = (MainActivity.this.pizzaBeanSel != null) ? MainActivity.this.pizzaBeanSel : pizzaBeanRead;
                pizzaBabyRead = (MainActivity.this.pizzaBabySel != null) ? MainActivity.this.pizzaBabySel : pizzaBabyRead;
                noteRead = (MainActivity.this.noteSel != null) ? MainActivity.this.noteSel : noteRead;
                trasportoDaPagareRead = (MainActivity.this.trasportoDaPagareSel != null) ? MainActivity.this.trasportoDaPagareSel : trasportoDaPagareRead;
                pagatoRead = (MainActivity.this.pagatoSel != null) ? MainActivity.this.pagatoSel : pagatoRead;
                MainActivity.this.pizzaBabySel = pizzaBabyRead;
                MainActivity.this.noteSel = noteRead;
                MainActivity.this.trasportoDaPagareSel = trasportoDaPagareRead;
                MainActivity.this.pagatoSel = pagatoRead;
//                String strTrasporto = pizzaBeanRead.isTrasporto() ? "Spese Trasporto: € 2,00" : "";

                // Aggiorna la HOME : pizza selezionata
                MainActivity.this.itemBeanPizzaSel = new ItemBean(pizzaBeanRead.getTitolo(), pizzaBeanRead.getDescrizione2(), "", pizzaBeanRead.getStrEuro(), R.drawable.pizza, 1);
                MainActivity.this.itemBeanPizzaSel.setPizzaBaby(pizzaBabyRead);
                MainActivity.this.itemBeanPizzaSel.setNote(noteRead);
                MainActivity.this.itemBeanPizzaSel.setTrasportoDaPagare(trasportoDaPagareRead);
                MainActivity.this.itemBeanPizzaSel.setPagato(pagatoRead);

                HomeFragment homeFragment = (HomeFragment) mapFragment.get(R.id.nav_home);
                lstItemBean.add(itemBeanPizzaSel);
//                MainActivity.this.lstItemBean =  homeFragment.getHomeViewModel().addItemBean(itemBeanPizzaSel);

                // Note
                MainActivity.this.itemBeanNoteSel = new ItemBean(null, noteRead, null, null, -1, 23);
                itemBeanNoteSel.setPizzaBaby(pizzaBabyRead);
                lstItemBean.add(itemBeanNoteSel);

                // Aggiorna la HOME : snack selezionato
                snackBeanRead = (MainActivity.this.snackBeanSel != null) ? MainActivity.this.snackBeanSel : snackBeanRead;
                MainActivity.this.itemBeanSnackSel = new ItemBean(snackBeanRead.getTitolo(), snackBeanRead.getDescrizione(), snackBeanRead.getDescrizione2(), snackBeanRead.getStrEuro(), snackBeanRead.getIdImmagine(), 2);
                MainActivity.this.itemBeanSnackSel.setTrasportoDaPagare(trasportoDaPagareRead);
                MainActivity.this.itemBeanSnackSel.setPagato(pagatoRead);
                lstItemBean.add(itemBeanSnackSel);

//                MainActivity.this.lstItemBean = lstItemBean;
//                homeFragment.getHomeViewModel().clearDataItemBean();
//                homeFragment.getHomeViewModel().changeDataItemBeanSel(lstItemBean);

//                if (reload) {
////                    homeFragment.getAdapterList().changeDataItemBeanSel(true, lstItemBean);
//                    // homeFragment.aggiornaData(lstItemBean);
//                } else {
//                    homeFragment.getAdapterList().changeDataItemBeanSel(false, lstItemBean);
//                }

                if (smoothProgressBar != null)
                    smoothProgressBar.setVisibility(View.INVISIBLE);
            }

            // --------------------------------------------------------------------------------------------
            // UPDATE
            // --------------------------------------------------------------------------------------------
            if (MainActivity.this.pizzaBeanSel != null) {
                List<CellData> val = new ArrayList<>();
                val.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue(MainActivity.this.pizzaBeanSel.getDescrizione())));
                val.add(new CellData().setUserEnteredValue(new ExtendedValue().setBoolValue(MainActivity.this.pizzaBabySel)));
                if (MainActivity.this.snackBeanSel != null) {
                    String strUpdate = Constant.NESSUN_SNACK_SELEZIONATO;
                    if (MainActivity.this.snackBeanSel.getPrezzo() != null && !MainActivity.this.snackBeanSel.getPrezzo().equals(BigDecimal.ZERO))
                        strUpdate = MainActivity.this.snackBeanSel.getDescrizione().toUpperCase() + " " + MainActivity.this.snackBeanSel.getDescrizione2() + " (€ " + MainActivity.this.snackBeanSel.getStrEuro() + ")";
                    val.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue(strUpdate)));
                }
                if (MainActivity.this.noteSel != null) {
                    val.add(new CellData().setUserEnteredValue(new ExtendedValue().setStringValue(MainActivity.this.noteSel)));
                }

                List<Request> requests = new ArrayList<>();
                requests.add(new Request()
                        .setUpdateCells(new UpdateCellsRequest()
                                .setStart(new GridCoordinate()
                                        .setSheetId(0)
                                        .setRowIndex(posNomeCognome)
                                        .setColumnIndex(1))
                                .setRows(Arrays.asList(new RowData().setValues(val)))
                                .setFields("userEnteredValue,userEnteredFormat.backgroundColor")));

                BatchUpdateSpreadsheetRequest batchUpdateRequest = new BatchUpdateSpreadsheetRequest()
                        .setRequests(requests);
                mService.spreadsheets().batchUpdate(Constant.ID_SHEET_PIZZA, batchUpdateRequest).execute();
            }
            // --------------------------------------------------------------------------------------------

            MainActivity.this.pizzaBeanSel = (pizzaBeanRead != null) ? pizzaBeanRead : MainActivity.this.pizzaBeanSel;
            MainActivity.this.snackBeanSel = (snackBeanRead != null) ? snackBeanRead : MainActivity.this.snackBeanSel;

            arr2Obj[0] = MainActivity.this.mDataList;
            arr2Obj[1] = lstItemBean;
            return arr2Obj;
        }

        @Override
        protected void onPostExecute(Object[] arr2Obj) {
            List<PizzaBean> output = (List<PizzaBean>) arr2Obj[0];

            if (MainActivity.this.posNomeCognome != 0) {
                getSupportActionBar().setTitle(MainActivity.this.nome + " " + MainActivity.this.cognome);
            } else {
                getSupportActionBar().setTitle("Nome non censito !!!");
                initVariabili();
                SmoothProgressBar smoothProgressBar = findViewById(R.id.progressbar);
                smoothProgressBar.progressiveStop();

                HomeFragment homeFragment = (HomeFragment) mapFragment.get(R.id.nav_home);
//                homeFragment.getHomeViewModel().changeDataItemBeanSel(new ArrayList<>());
                homeFragment.getAdapterList().changeDataItemBeanSel(true, new ArrayList<>());
            }

            if (output == null || output.size() == 0) {
                output = new ArrayList<PizzaBean>();
                output.add(0, new PizzaBean("Nessuna pizza in listino !!!", BigDecimal.ZERO));
            } else {
                if (output.get(0) != null && !Constant.NESSUNA_PIZZA_SELEZIONATA.equals(output.get(0).getDescrizione())) {
                    PizzaBean pizzaBeanNo = new PizzaBean(Constant.NESSUNA_PIZZA_SELEZIONATA, "", "0", false);
                    pizzaBeanNo.setPrezzo(BigDecimal.ZERO);

                    output.add(0, pizzaBeanNo);
                }

                if (arr2Obj != null && arr2Obj.length >= 2 && arr2Obj[1] != null) {
                    HomeFragment homeFragment = (HomeFragment) mapFragment.get(R.id.nav_home);
                    List<ItemBean> lstItemBean = (List<ItemBean>) arr2Obj[1];

//                    if (this.reload) {
                        try {
//                          homeFragment.aggiornaData(lstItemBean);
//                            homeFragment.getHomeViewModel().getDataItemBean().postValue(lstItemBean);
//                            homeFragment.getAdapterList().changeDataItemBeanSel(true, lstItemBean);

                            if (homeFragment.getAdapterList() == null) {
                                aggiornaMapFragment();
                                homeFragment = (HomeFragment) mapFragment.get(R.id.nav_home);
                            }

                            homeFragment.getAdapterList().changeDataItemBeanSel(false, null);
                            homeFragment.getAdapterList().changeDataItemBeanSel(false, lstItemBean);

                            SmoothProgressBar smoothProgressBar = findViewById(R.id.progressbar);
                            smoothProgressBar.progressiveStop();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
//                    } else {
//                        homeFragment.getAdapterList().changeDataItemBeanSel(false, lstItemBean);
//                    }
                }
            }
        }

        @Override
        protected void onCancelled() {
//            mProgress.hide();
            if (mLastError != null) {
                if (mLastError instanceof GooglePlayServicesAvailabilityIOException) {
                    showGooglePlayServicesAvailabilityErrorDialog(
                            ((GooglePlayServicesAvailabilityIOException) mLastError)
                                    .getConnectionStatusCode());
                } else if (mLastError instanceof UserRecoverableAuthIOException) {
                    startActivityForResult(
                            ((UserRecoverableAuthIOException) mLastError).getIntent(),
                            MainActivity.REQUEST_AUTHORIZATION);
                } else {
//                    mOutputText.setText("The following error occurred:\n"
//                            + mLastError.getMessage());
                }
            } else {
//                mOutputText.setText("Request cancelled.");
            }
        }
    }

}
