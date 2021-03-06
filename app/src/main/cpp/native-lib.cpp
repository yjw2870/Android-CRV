#define NO_PROCPS
#define CURVE_ALT_BN128
//#define MIE_ATE_USE_GMP
#define NDEBUG
//#define _FILE_OFFSET_BITS 64
//#define MIE_ZM_VUINT_BIT_LEN (64 * 9)
//#define NDEBUG 1
#include <android/log.h>
#define  LOG_TAG    "NDK_TEST"
#define  LOGD(...)  __android_log_print(ANDROID_LOG_DEBUG,LOG_TAG,__VA_ARGS__)

#include <jni.h>
#include <string>
#include <iostream>
#include <gmp.h>
#include <gmpxx.h>
#include <openssl/bn.h>
#include <cassert>
#include <cstdio>
#include <math.h>
#include <string.h>
#include <stdlib.h>
#include <sstream>
#include <type_traits>
#include <libff/common/profiling.hpp>
#include <libff/common/utils.hpp>
#include <libff/algebra/curves/public_params.hpp>
#include <libsnark/common/default_types/r1cs_gg_ppzksnark_pp.hpp>
#include <libsnark/relations/constraint_satisfaction_problems/r1cs/examples/r1cs_examples.hpp>
#include <libsnark/jsnark_interface/CircuitReader.hpp>
//#include "CircuitReader.hpp"
#include <libsnark/gadgetlib2/integration.hpp>
#include <libsnark/gadgetlib2/adapters.hpp>
#include <libsnark/zk_proof_systems/ppzksnark/voting/r1cs_gg_ppzksnark.hpp>
#include <libsnark/zk_proof_systems/ppzksnark/voting/run_r1cs_gg_ppzksnark.hpp>
// #include <libsnark/zk_proof_systems/ppzksnark/voting/r1cs_gg_ppzksnark.hpp>

#include <libsnark/common/default_types/r1cs_gg_ppzksnark_pp.hpp>
#include <unistd.h>

using namespace libsnark;
using namespace std;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_snarkportingtest_SubActivity_stringFromJNI(
        JNIEnv* env,
        jobject jobj,
        jstring task, jstring mode, jstring location
        ) {

    libff::start_profiling();
    gadgetlib2::initPublicParamsFromDefaultPp();
    gadgetlib2::GadgetLibAdapter::resetVariableIndex();
    ProtoboardPtr pb = gadgetlib2::Protoboard::create(gadgetlib2::R1P);

    const char *task_ = (env)->GetStringUTFChars(task, NULL);
    const char *mode_ = (env)->GetStringUTFChars(mode, NULL);
    const char *loc_ = (env)->GetStringUTFChars(location, NULL);

    char *path1, *path2;
    LOGD("task : %s", task_);
    if(strcmp("register", task_) == 0) {
        path1 = "/data/data/com.example.snarkportingtest/files/registerarith.txt";
        path2 = "/data/data/com.example.snarkportingtest/files/registerin.txt";
    }
    else if(strcmp("vote", task_) == 0) {
        path1 = "/data/data/com.example.snarkportingtest/files/votearith.txt";
        path2 = "/data/data/com.example.snarkportingtest/files/votein.txt";
    }
    else if(strcmp("tally", task_) == 0) {
        path1 = "/data/data/com.example.snarkportingtest/files/tallyarith.txt";
        path2 = "/data/data/com.example.snarkportingtest/files/tallyin.txt";
    }
    // Read the circuit, evaluate, and translate constraints
    LOGD("path : %s, %s", path1, path2);
    CircuitReader reader(path1, path2, pb);
    LOGD("circuit read done");
    r1cs_constraint_system<FieldT> cs = get_constraint_system_from_gadgetlib2(
            *pb);
    const r1cs_variable_assignment<FieldT> full_assignment =
            get_variable_assignment_from_gadgetlib2(*pb);
    cs.primary_input_size = reader.getNumInputs() + reader.getNumOutputs();
    cs.auxiliary_input_size = full_assignment.size() - cs.num_inputs();
    LOGD("%d %d\n", cs.primary_input_size, cs.auxiliary_input_size);
    // extract primary and auxiliary input
    const r1cs_primary_input<FieldT> primary_input(full_assignment.begin(),
                                                   full_assignment.begin() + cs.num_inputs());
    const r1cs_auxiliary_input<FieldT> auxiliary_input(
            full_assignment.begin() + cs.num_inputs(), full_assignment.end());


    // A follow-up will be added.
    if(!cs.is_satisfied(primary_input, auxiliary_input)){
        LOGD("The constraint system is  not satisifed by the value assignment - Terminating.");
    }

    r1cs_example<FieldT> example(cs, primary_input, auxiliary_input);
    const bool test_serialization = false;
    bool successBit = false;
    char *name1;
    LOGD("mode : %s\n", mode_);

    string name = task_;

    if(strcmp(mode_, "setup") == 0)
    {
        LOGD("setup");

        libsnark::run_r1cs_gg_ppzksnark_setup<libsnark::default_r1cs_gg_ppzksnark_pp>(example, test_serialization, name);

    }
    else if(strcmp(mode_, "verify") == 0)
    {
        LOGD("verify");
            // The following code makes use of the observation that
            // libsnark::default_r1cs_gg_ppzksnark_pp is the same as libff::default_ec_pp (see r1cs_gg_ppzksnark_pp.hpp)
            // otherwise, the following code won't work properly, as GadgetLib2 is hardcoded to use libff::default_ec_pp.
            successBit = libsnark::run_r1cs_gg_ppzksnark_verify<libsnark::default_r1cs_gg_ppzksnark_pp>(
                    example, test_serialization, name);

        if(!successBit){
            LOGD("Problem occurred while running the ppzksnark algorithms .. ");

        }

    }
    else if (strcmp(mode_, "run") == 0)
    {
        LOGD("run");

        // The following code makes use of the observation that
            // libsnark::default_r1cs_gg_ppzksnark_pp is the same as libff::default_ec_pp (see r1cs_gg_ppzksnark_pp.hpp)
            // otherwise, the following code won't work properly, as GadgetLib2 is hardcoded to use libff::default_ec_pp.
            libsnark::run_r1cs_gg_ppzksnark<libsnark::default_r1cs_gg_ppzksnark_pp>(
                    example, test_serialization, name);

    }
    else if(strcmp(mode_, "all") == 0) {
        LOGD("all");

        successBit = libsnark::run_r1cs_gg_ppzksnark_all<libsnark::default_r1cs_gg_ppzksnark_pp>(
                example, test_serialization, name);


        if (!successBit) {
            LOGD("Problem occurred while running the ppzksnark algorithms .. " );

        }

    }

    return env->NewStringUTF("1");
}