package nh.glazelog;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.Selection;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.toptoche.searchablespinnerlibrary.SearchableSpinner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import nh.glazelog.database.DBHelper;
import nh.glazelog.database.FiringCycleTextSaver;
import nh.glazelog.database.IngredientSpinnerSaver;
import nh.glazelog.database.IngredientTextSaver;
import nh.glazelog.database.SimpleSpinnerSaver;
import nh.glazelog.database.StaticSaver;
import nh.glazelog.database.TextSaver;
import nh.glazelog.glaze.Cone;
import nh.glazelog.glaze.Glaze;
import nh.glazelog.glaze.Ingredient;
import nh.glazelog.glaze.IngredientQuantity;
import nh.glazelog.glaze.RampHold;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_FIRST_USER;
import static android.app.Activity.RESULT_OK;
import static android.view.View.GONE;

/**
 * Created by Nick Hansen on 10/18/2017.
 */

public class VersionFragment extends Fragment {

    Glaze gVer;
    int verNum;
    LayoutInflater inflater;
    private View page;

    private final static int KEY_REQUEST_IMAGE_CAPTURE = 1;

    private TextView versionField;
    private EditText primaryNotes;
    private ImageView deleteVersion;
    private ImageView testTileImage;
    private File imageFile;
    private Uri imageUri;
    private EditText spgrField;
    private TableLayout recipeMaterialsTable;
    private Button materialsAddLineButton;
    private TableLayout recipeAdditionsTable;
    private Button additionsAddLineButton;
    private Spinner bisqueSpinner;
    private TableLayout firingCycleTable;
    private Button firingCycleAddLineButton;
    private EditText secondaryNotes;

    @Override
    public View onCreateView (final LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        Bundle args = getArguments();
        gVer = args.getParcelable(VersionPagerAdapter.KEY_GLAZE_VERSION);
        verNum = args.getInt(VersionPagerAdapter.KEY_GLAZE_VERSION_NUMBER) + 1;
        this.inflater = inflater;
        page = inflater.inflate(R.layout.versionfragment_single, container, false);

        versionField = (TextView) page.findViewById(R.id.versionField);
        versionField.setText(new Integer(verNum).toString());

        primaryNotes = (EditText) page.findViewById(R.id.primaryNotes);
        primaryNotes.setText(gVer.getPrimaryNotes());
        primaryNotes.addTextChangedListener(new TextSaver(getContext(),gVer,DBHelper.SingleCN.PRIMARY_NOTES,false,false));

        deleteVersion = (ImageView) page.findViewById(R.id.deleteVersionImageView);
        if (verNum == 1) deleteVersion.setVisibility(GONE);
        else {
            deleteVersion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ConfirmDialog confirmDelete = new ConfirmDialog(getContext(), true, "to delete this version? \nWARNING: this CANNOT be undone.",
                            new ConfirmDialog.Action() {
                        @Override
                        public void action() {
                            deleteVersion();
                        }
                    });
                    confirmDelete.show();
                }
            });
        }

        testTileImage = (ImageView) page.findViewById(R.id.testTileImageView);
        if (!gVer.getImageUri().equals(Uri.EMPTY)) testTileImage.setImageURI(gVer.getImageUri());
        testTileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (canReadWrite()) dispatchCameraIntent();
                else requestStoragePermissions();
            }
        });

        spgrField = (EditText) page.findViewById(R.id.spgrField);
        spgrField.setText(new Double(gVer.getSpgr()).toString());
        spgrField.addTextChangedListener(new TextSaver(getContext(),gVer,DBHelper.SingleCN.SPGR,false,false));

        recipeMaterialsTable = (TableLayout) page.findViewById(R.id.recipeMaterialsTable);
        ArrayList<IngredientQuantity> materialsList = gVer.getMaterials();
        if (materialsList.size() == 0)
            addRecipeRow(null,true);
        else
            for (IngredientQuantity iq : materialsList) addRecipeRow(iq,true);
        materialsAddLineButton = (Button) page.findViewById(R.id.materialAddLineButton);
        materialsAddLineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                addRecipeRow(null,true);
                fixTables();
            }
        });

        recipeAdditionsTable = (TableLayout) page.findViewById(R.id.recipeAdditionsTable);
        ArrayList<IngredientQuantity> additionsList = gVer.getAdditions();
        if (additionsList.size() == 0)
            addRecipeRow(null,false);
        else
            for (IngredientQuantity iq : additionsList) addRecipeRow(iq,false);
        additionsAddLineButton = (Button) page.findViewById(R.id.additionsAddLineButton);
        additionsAddLineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                addRecipeRow(null, false);
                fixTables();
            }
        });

        bisqueSpinner = (Spinner) page.findViewById(R.id.bisqueSpinner);
        bisqueSpinner.setAdapter(new ArrayAdapter<Cone>(this.getContext(),R.layout.spinner_item_small,Cone.values()));
        Util.setSpinnerSelection(bisqueSpinner,gVer.getBisquedTo()); // default selection
        bisqueSpinner.setOnItemSelectedListener(new SimpleSpinnerSaver(getContext(),gVer,DBHelper.CCN_BISQUED_TO,false));

        firingCycleTable = (TableLayout) page.findViewById(R.id.firingcycleTable);
        ArrayList<RampHold> firingCycle = gVer.getFiringCycle();
        if (firingCycle.size() == 0)
            addFiringCycleRow(null);
        else
            for (RampHold rh : firingCycle) addFiringCycleRow(rh);
        firingCycleAddLineButton = (Button) page.findViewById(R.id.firingCycleAddLineButton);
        firingCycleAddLineButton.setOnClickListener(new View.OnClickListener() {
            public void onClick (View v) {
                addFiringCycleRow(null);
                fixTables();
            }
        });

        secondaryNotes = (EditText) page.findViewById(R.id.secondaryNotes);
        secondaryNotes.setText(gVer.getSecondaryNotes());
        secondaryNotes.addTextChangedListener(new TextSaver(getContext(),gVer,DBHelper.SingleCN.SECONDARY_NOTES,false,false));



        return page;
    }


    /*--------------------ADD TABLE ROWS--------------------*/

    /* For some ungodly reason
     * whenever I add rows to any of the tables
     * the others glitch the fuck out.
     * I thought this was an emulator issue
     * but it also happened on my phone,
     * so I had to implement this to fix the issue.
     * When I originally implemented the tables
     * this wasn't a problem.
     * I have no idea where it came from
     * and no idea how to actually fix the root cause of it.
     * This is more of a quick fix, but it works.
     *
     * Quote me on that.
     *
     * Nick Hansen 11/12/17
     *
     *
     *
     * Apparently it occurs not just when adding rows
     * but whenever I edit any table at ALL
     * (i.e. removing rows causes it as well).
     * I've added this to the remove table image buttons as well.
     *
     * Nick Hansen 11/13/17
     */
    private void fixTables () {
        recipeMaterialsTable.requestLayout();
        recipeAdditionsTable.requestLayout();
        firingCycleTable.requestLayout();
    }

    private void addRecipeRow (IngredientQuantity iq, final boolean isMaterials) {
        final TableRow recipeRow = (TableRow) inflater.inflate(R.layout.tablerow_recipe,null);
        final ConfirmDialog confirmDeleteDialog = new ConfirmDialog(getContext(), true, "to delete this row?",
                new ConfirmDialog.Action() {
                    @Override
                    public void action() {
                        if (isMaterials) {
                            recipeMaterialsTable.removeView(recipeRow);
                            StaticSaver.ingredientWithoutInstance(getContext(),gVer,recipeMaterialsTable,isMaterials);
                        }
                        else {
                            recipeAdditionsTable.removeView(recipeRow);
                            StaticSaver.ingredientWithoutInstance(getContext(),gVer,recipeAdditionsTable,isMaterials);
                        }
                        fixTables();
                    }
                });

        SearchableSpinner ingredient = (SearchableSpinner) recipeRow.findViewById(R.id.ingredientEditText);
        ingredient.setAdapter(new ArrayAdapter<Ingredient>(this.getContext(),/*android.R.layout.select_dialog_item*/R.layout.spinner_item_small, Ingredient.values()));
        final EditText amount = (EditText) recipeRow.findViewById(R.id.amountEditText);
        amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                System.out.println("FOCUS: " + hasFocus);
                if (hasFocus) {
                    amount.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            System.out.println("HAS FOCUS");
                            amount.setSelection(amount.getText().length());
                            System.out.println("TEXT LENGTH: " + amount.getText().length());
                        }
                    },50);
                }
            }
        });
        ImageView deleteRow = (ImageView) recipeRow.findViewById(R.id.deleteRowImageView);
        deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog.show();
            }
        });

        if (iq == null) {
            amount.setText("");
            amount.setHint("0");
        }
        else {
            Util.setSpinnerSelection(ingredient,iq.getIngredient());
            amount.setText(new Double(iq.getAmount()).toString());
        }

        if (isMaterials) {
            ingredient.setOnItemSelectedListener(new IngredientSpinnerSaver(getContext(),gVer,recipeMaterialsTable,isMaterials));
            amount.addTextChangedListener(new IngredientTextSaver(getContext(),gVer,recipeMaterialsTable,isMaterials));
        }
        else {
            ingredient.setOnItemSelectedListener(new IngredientSpinnerSaver(getContext(),gVer,recipeAdditionsTable,isMaterials));
            amount.addTextChangedListener(new IngredientTextSaver(getContext(),gVer,recipeAdditionsTable,isMaterials));
        }

        if (isMaterials)    recipeMaterialsTable.addView(recipeRow);
        else                recipeAdditionsTable.addView(recipeRow);
    }

    private void addFiringCycleRow(RampHold rh) {
        final TableRow firingCycleRow = (TableRow) inflater.inflate(R.layout.tablerow_firingcycle,null);
        final ConfirmDialog confirmDeleteDialog = new ConfirmDialog(getContext(), true, "to delete this row?",
                new ConfirmDialog.Action() {
                    @Override
                    public void action() {
                        firingCycleTable.removeView(firingCycleRow);
                        StaticSaver.firingCycleWithoutInstance(getContext(),gVer,firingCycleTable);
                        fixTables();
                    }
                });

        EditText temperature = (EditText) firingCycleRow.findViewById(R.id.temperatureEditText);
        EditText rate = (EditText) firingCycleRow.findViewById(R.id.rateEditText);
        EditText hold = (EditText) firingCycleRow.findViewById(R.id.holdEditText);
        ImageView deleteRow = (ImageView) firingCycleRow.findViewById(R.id.deleteRowImageView);
        deleteRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDeleteDialog.show();
            }
        });

        if (rh == null) {
            temperature.setText("");
            rate.setText("");
            hold.setText("");
        }
        else {
            temperature.setText(new Double(rh.getTemperature()).toString());
            rate.setText(new Double(rh.getRate()).toString());
            hold.setText(new Double(rh.getHold()).toString());
        }

        temperature.addTextChangedListener(new FiringCycleTextSaver(getContext(),gVer,firingCycleTable));
        rate.addTextChangedListener(new FiringCycleTextSaver(getContext(),gVer,firingCycleTable));
        hold.addTextChangedListener(new FiringCycleTextSaver(getContext(),gVer,firingCycleTable));

        firingCycleTable.addView(firingCycleRow);
    }


    /*--------------------ADDING AND REMOVING VERSIONS--------------------*/

    VersionPager versionPager;
    VersionPagerAdapter versionPagerAdapter;
    // these two MUST be instantiated inside each function to avoid a NullPointerException

    private void addNewVersion() {
        if (versionPager == null) versionPager = (VersionPager) getActivity().findViewById(R.id.versionPager);
        if (versionPagerAdapter == null) versionPagerAdapter = versionPager.getVersionPagerAdapter();
        versionPagerAdapter.addPage();
        versionPager.setCurrentItem(versionPagerAdapter.getCount()-1);
    }

    private void deleteVersion () {
        if (versionPager == null) versionPager = (VersionPager) getActivity().findViewById(R.id.versionPager);
        if (versionPagerAdapter == null) versionPagerAdapter = versionPager.getVersionPagerAdapter();
        versionPager.setCurrentItem(verNum-2,true);
        versionPagerAdapter.deletePage(gVer);
    }




    /*--------------------TAKE AND SAVE PICTURE OF TEST TILE--------------------*/


    private boolean canReadWrite() {
        boolean checkReadPermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        boolean checkWritePermission = ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
        return checkReadPermission && checkWritePermission;
    }

    private final static int PERMISSION_USE_INTERNAL_STORAGE = 200;
    private void requestStoragePermissions() {
        //Manifest.permission.READ_EXTERNAL_STORAGE
        ActivityCompat.requestPermissions(getActivity(),
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                PERMISSION_USE_INTERNAL_STORAGE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        System.out.println("REQUEST CODE MATCHES: " + (requestCode == PERMISSION_USE_INTERNAL_STORAGE));
        System.out.println("PERMISSION GRANTED: " + (grantResults[0] == PackageManager.PERMISSION_GRANTED));
        if (requestCode == PERMISSION_USE_INTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                dispatchCameraIntent();
    }


    public void dispatchCameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if (intent.resolveActivity(getContext().getPackageManager()) != null) {
            startActivityForResult(intent, KEY_REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String resultCodeType = "UNKNOWN RESULT CODE";
        if (resultCode == RESULT_OK) resultCodeType = "RESULT_OK";
        else if (resultCode == RESULT_CANCELED) resultCodeType = "RESULT_CANCELED";
        else if (resultCode == RESULT_FIRST_USER) resultCodeType = "RESULT_FIRST_USER";
        System.out.println("Request code matches take image key: " + (requestCode == KEY_REQUEST_IMAGE_CAPTURE));
        System.out.println("Img Intent result code: " + resultCodeType);

        if (requestCode == KEY_REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");


            imageFile = new File(Environment.getExternalStorageDirectory() + getString(R.string.__imagespath),gVer.getCreationDateRaw() + ".png");
            FileOutputStream outputStream = null;
            if (imageFile.exists()) imageFile.delete();
            else imageFile.mkdirs();

            try{
                imageFile.createNewFile();
                outputStream = new FileOutputStream(imageFile);
                System.out.println("Before compress: bmp height: " + imageBitmap.getHeight() + " bmp width: " + imageBitmap.getWidth());
                imageBitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                System.out.println("After compress: bmp height: " + imageBitmap.getHeight() + " bmp width: " + imageBitmap.getWidth());
            } catch (IOException e) {
                e.printStackTrace();
                onActivityResult(requestCode,resultCode,data);
            } finally {
                try {
                    if (outputStream != null) {
                        outputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                imageUri = Uri.fromFile(imageFile);
                System.out.println("Uses old Uri format: " + imageUri);
            } else {
                imageUri = FileProvider.getUriForFile(getContext(), getContext().getPackageName() + ".fileprovider", imageFile);
                System.out.println("Uses Android N+ Uri format: " + imageUri);
            }

            ContentValues imageUriCV= new ContentValues();
            imageUriCV.put(DBHelper.SingleCN.IMAGE_URI_STRING,imageUri.toString());
            DBHelper.getSingletonInstance(getContext()).append(gVer,imageUriCV);
            System.out.println("Image saved with Uri " + imageUri.toString());
            testTileImage.setImageURI(imageUri);
        }
    }

}
