package com.sequoiadb.hadoop.mapreduce;

import java.io.IOException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configurable;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.InputSplit;
import org.apache.hadoop.mapreduce.JobContext;
import org.apache.hadoop.mapreduce.RecordReader;
import org.apache.hadoop.mapreduce.TaskAttemptContext;
import org.bson.BSONObject;

import com.sequoiadb.hadoop.io.BSONWritable;
import com.sequoiadb.hadoop.io.SequoiadbBlockReader;
import com.sequoiadb.hadoop.io.SequoiadbIndexReader;
import com.sequoiadb.hadoop.split.SdbBlockSplit;
import com.sequoiadb.hadoop.split.SdbIndexSplit;
import com.sequoiadb.hadoop.split.SdbSplitFactory;
import com.sequoiadb.hadoop.util.SequoiadbConfigUtil;

/**
 * 
 * 
 * @className锛歋equoiadbInputFormat
 * 
 * @author锛�gaoshengjie
 * 
 * @createtime:2013骞�2鏈�1鏃�涓婂崍10:14:14
 * 
 * @changetime:TODO
 * 
 * @version 1.0.0
 * 
 */
public class SequoiadbInputFormat extends
		InputFormat<Object, BSONWritable> implements Configurable {
	private static final Log log = LogFactory
			.getLog(SequoiadbInputFormat.class);

	private  String collectionSpaceName;
	private  String collectionName;
	private Configuration conf;

	public SequoiadbInputFormat() {
		
	}

	@Override
	public RecordReader<Object, BSONWritable> createRecordReader(
			InputSplit inputSplit, TaskAttemptContext taskAttemptContext)
			throws IOException, InterruptedException {
		if (inputSplit instanceof SdbBlockSplit) {
			return new SequoiadbBlockReader(inputSplit, this.conf);
		} else if (inputSplit instanceof SdbIndexSplit) {
			return new SequoiadbIndexReader(inputSplit, this.conf);
		} else {
			throw new IllegalArgumentException(
					"the type of inputSplit is wrong,only support SdbBlockSplit and SdbIndexSplit");
		}

	}

	@Override
	public List<InputSplit> getSplits(JobContext jobContext)
			throws IOException, InterruptedException {
		List<InputSplit>  inputSplits = SdbSplitFactory.getSplits(jobContext);
		log.info("inputSplits.size"+inputSplits.size());
		return inputSplits;
	}

	@Override
	public Configuration getConf() {
		return this.conf;
	}

	@Override
	public void setConf(Configuration configuration) {
		this.conf=configuration;
	}

}
