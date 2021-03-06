package hex.tree.gbm;

import hex.tree.SharedTreeModel;
import water.Key;
import water.fvec.Chunk;
import water.util.ModelUtils;
import water.util.SB;

public class GBMModel extends SharedTreeModel<GBMModel,GBMModel.GBMParameters,GBMModel.GBMOutput> {

  public static class GBMParameters extends SharedTreeModel.SharedTreeParameters {
    /** Distribution functions.  Note: AUTO will select gaussian for
     *  continuous, and multinomial for categorical response
     *
     *  <p>TODO: Replace with drop-down that displays different distributions
     *  depending on cont/cat response
     */
    public enum Family {  AUTO, bernoulli, multinomial, gaussian  }
    public Family _loss = Family.AUTO;
    public float _learn_rate=0.1f; // Learning rate from 0.0 to 1.0
  }

  public static class GBMOutput extends SharedTreeModel.SharedTreeOutput {
    public GBMOutput( GBM b, double mse_train, double mse_valid ) { super(b,mse_train,mse_valid); }
  }

  public GBMModel(Key selfKey, GBMParameters parms, GBMOutput output ) { super(selfKey,parms,output); }

  /** Bulk scoring API for one row.  Chunks are all compatible with the model,
   *  and expect the last Chunks are for the final distribution and prediction.
   *  Default method is to just load the data into the tmp array, then call
   *  subclass scoring logic. */
  @Override public double[] score0( Chunk chks[], int row_in_chunk, double[] tmp, double[] preds ) {
    assert chks.length>=tmp.length;
    for( int i=0; i<tmp.length; i++ )
      tmp[i] = chks[i].atd(row_in_chunk);
    return score0(tmp,preds);
  }

  @Override protected double[] score0(double data[/*ncols*/], double preds[/*nclasses+1*/]) {
    super.score0(data, preds);    // These are f_k(x) in Algorithm 10.4
    if( _parms._loss == GBMParameters.Family.bernoulli ) {
      double fx = preds[1] + _output._initialPrediction;
      preds[2] = 1.0/(1.0+Math.exp(-fx));
      preds[1] = 1.0-preds[2];
      ModelUtils.correctProbabilities(preds, _output._priorClassDist, _output._modelClassDist);
      preds[0] = hex.genmodel.GenModel.getPrediction(preds, data);
      return preds;
    }
    if( _output.nclasses()==1 ) {
      // Prediction starts from the mean response, and adds predicted residuals
      preds[0] += _output._initialPrediction;
      return preds;
    }
    if( _output.nclasses()==2 ) { // Kept the initial prediction for binomial
      preds[1] += _output._initialPrediction;
      preds[2] = - preds[1];
    }
    hex.genmodel.GenModel.GBM_rescale(data, preds);
    ModelUtils.correctProbabilities(preds, _output._priorClassDist, _output._modelClassDist);
    preds[0] = hex.genmodel.GenModel.getPrediction(preds, data);
    return preds;
  }

  @Override protected boolean binomialOpt() { return true; }

  @Override protected void toJavaUnifyPreds(SB body, SB file) {
    // Preds are filled in from the trees, but need to be adjusted according to
    // the loss function.
    if( _parms._loss == GBMParameters.Family.bernoulli ) {
      body.ip("double fx = preds[1] + ").p(_output._initialPrediction).p(";").nl();
      body.ip("preds[2] = 1.0/(1.0+Math.exp(-fx));").nl();
      body.ip("preds[1] = 1.0-preds[2];").nl();
      body.ip("water.util.ModelUtils.correctProbabilities(preds, PRIOR_CLASS_DISTRIB, MODEL_CLASS_DISTRIB);").nl();
      body.ip("preds[0] = hex.genmodel.GenModel.getPrediction(preds, data);").nl();
      return;
    }
    if( _output.nclasses() == 1 ) { // Regression
      // Prediction starts from the mean response, and adds predicted residuals
      body.ip("preds[0] += ").p(_output._initialPrediction).p(";");
      return;
    }
    if( _output.nclasses()==2 ) { // Kept the initial prediction for binomial
      body.ip("preds[1] += ").p(_output._initialPrediction).p(";").nl();
      body.ip("preds[2] = - preds[1];").nl();
    }
    body.ip("hex.genmodel.GenModel.GBM_rescale(data,preds);").nl();
    body.ip("water.util.ModelUtils.correctProbabilities(preds, PRIOR_CLASS_DISTRIB, MODEL_CLASS_DISTRIB);").nl();
    body.ip("preds[0] = hex.genmodel.GenModel.getPrediction(preds, data);").nl();
  }
}
