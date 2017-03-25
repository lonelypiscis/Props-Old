package lonelypiscis.props.resource;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.jws.WebParam.Mode;

import org.spongepowered.api.resourcepack.ResourcePack;
import org.spongepowered.api.resourcepack.ResourcePacks;

import com.google.common.io.Files;
import com.google.gson.JsonObject;

import lonelypiscis.props.prop.ModelData;
import lonelypiscis.props.utils.Debug;
import lonelypiscis.props.utils.FileUtils;
import lonelypiscis.props.utils.ResourceUtils;
import lonelypiscis.props.utils.ZipUtils;

public class ResourceFactory {
	private String PATH_TMP_DIR = ResourceUtils.getFullAssetPath("tmp/");
	private String PATH_TEMPLATE_FILE = ResourceUtils.getFullAssetPath("tmp/prop_resources_template");
	private String PATH_MODEL_DIR = ResourceUtils.getFullAssetPath("propModels/");
	private String TMP_FILE_PREFIX = "props_resources_";

	private String tmpRespackRoot;
	private String tmpRespackFileName;
	private String path_tmpModelsDir;
	private String path_zipFile;
	private File tmpRespackFile;
	private File templateRespackFile;

	private ArrayList<ModelData> modelDatas;

	public ResourceFactory(ArrayList<ModelData> models) {
		modelDatas = models;
	}

	/**
	 * Returns the resource pack's filename. Creates a new one when it has not
	 * been set yet.
	 * 
	 * @return the pack's filename
	 */

	private String getRespackFileName() {
		if (tmpRespackFileName == null || tmpRespackFileName.equals("")) {
			DateFormat df = new SimpleDateFormat("MMddyyyy-HHmmss");
			Date today = Calendar.getInstance().getTime();

			tmpRespackFileName = TMP_FILE_PREFIX + df.format(today);
		}

		return tmpRespackFileName;
	}

	public ResourcePack build() throws FileNotFoundException {
		Builder respackBuilder = new Builder();
		
		respackBuilder.copyTemplateFile();
		respackBuilder.copyModelFiles();
		respackBuilder.overrideModels();
		respackBuilder.createZipFile();

		return ResourcePacks.fromUri(tmpRespackFile.toURI());
	}

	class Builder {
		private void copyTemplateFile() {
			tmpRespackRoot = PATH_TMP_DIR + getRespackFileName() + "/";
			path_tmpModelsDir = tmpRespackRoot + "assets/minecraft/models/item/";

			templateRespackFile = new File(PATH_TEMPLATE_FILE);
			tmpRespackFile = new File(tmpRespackRoot);

			try {
				FileUtils.copyFolder(templateRespackFile, tmpRespackFile);

				Debug.getLogger().info("Temporary resource pack has been created as " + tmpRespackRoot);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void copyModelFiles() {
			FileUtils.moveContent(PATH_MODEL_DIR, tmpRespackRoot + "assets/minecraft/models/item/");
		}

		private void overrideModels() {
			Debug.getLogger().info(modelDatas.size() + " models to override.");

			ModelData model1 = modelDatas.get(0);
			File overrideTarget = new File(path_tmpModelsDir + model1.getOverrideTarget() + ".json");

			// Open the target file
			try {
				Debug.getLogger().info("Writing to " + overrideTarget);

				BufferedWriter targetWriter = new BufferedWriter(new FileWriter(overrideTarget));

				targetWriter.write("{\"parent\": \"item/generated\", \"textures\": { \"layer0\": \"item/");
				targetWriter.write(model1.getOverrideTarget());
				targetWriter.write("\" }, \"overrides\": [");

				ModelData modelLast = modelDatas.get(modelDatas.size() - 1);

				for (ModelData model : modelDatas) {
					targetWriter.write("{ \"predicate\": { \"damaged\": 1, \"damage\":");
					targetWriter.write(String.valueOf(model.getDamagePercentage()));
					targetWriter.write("}, \"model\": \"item/");
					targetWriter.write(model.getModelName());
					targetWriter.write("\"}");

					if (modelLast != model) {
						targetWriter.write(",");
					}
				}

				targetWriter.write("]}");

				targetWriter.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		private void createZipFile() {
			path_zipFile = PATH_TMP_DIR + tmpRespackFile.getName() + ".zip";

			ZipUtils zipUtils = new ZipUtils(PATH_TMP_DIR);
			zipUtils.zipIt(path_zipFile);
		}

	}
}
