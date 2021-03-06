package water.api;

import hex.ModelMetricsMultinomial;
import static hex.ModelMetricsMultinomial.getHitRatioTable;
import water.util.TwoDimTable;

public class ModelMetricsMultinomialV3 extends ModelMetricsBase<ModelMetricsMultinomial, ModelMetricsMultinomialV3> {
  @API(help="The Mean Squared Error of the prediction for this scoring run.", direction=API.Direction.OUTPUT)
  public double mse;

  @API(help="The hit ratios for this scoring run.", direction=API.Direction.OUTPUT)
  public float[] hit_ratios;

  @API(help="The hit ratio table for this scoring run.", direction=API.Direction.OUTPUT)
  public TwoDimTableBase hit_ratio_table;

  @API(help="The ConfusionMatrix object for this scoring run.", direction=API.Direction.OUTPUT)
  public ConfusionMatrixBase cm;

  @API(help="The logarithmic loss for this scoring run.", direction=API.Direction.OUTPUT)
  public double logloss;

  @Override public ModelMetricsMultinomialV3 fillFromImpl(ModelMetricsMultinomial modelMetrics) {
    super.fillFromImpl(modelMetrics);
    mse = modelMetrics._mse;
    hit_ratios = modelMetrics._hit_ratios;
    logloss = modelMetrics._logloss;

    if (hit_ratios != null) {
      TwoDimTable table = getHitRatioTable(hit_ratios);
      hit_ratio_table = (TwoDimTableBase)Schema.schema(this.getSchemaVersion(), table).fillFromImpl(table);
    }

    if (null != modelMetrics._cm) {
      modelMetrics._cm.table();  // Fill in lazy table, for icing
      cm = (ConfusionMatrixBase) Schema.schema(this.getSchemaVersion(), modelMetrics._cm).fillFromImpl(modelMetrics._cm);
    }

    return this;
  }
}
