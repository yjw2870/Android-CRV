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


using namespace libsnark;
using namespace std;

extern "C" JNIEXPORT jstring JNICALL
Java_com_example_snarkportingtest_MainActivity_stringFromJNI(
        JNIEnv* env,
        jobject jobj,
        jstring task, jstring mode) {

//    libff::alt_bn128_pp::init_public_params();

//    libff::start_profiling();

    libff::start_profiling();
    gadgetlib2::initPublicParamsFromDefaultPp();
    gadgetlib2::GadgetLibAdapter::resetVariableIndex();
    ProtoboardPtr pb = gadgetlib2::Protoboard::create(gadgetlib2::R1P);

    int inputStartIndex = 0;
//    if(argc == 6){
//        if(strcmp(argv[1], "gg") != 0){
//            cout << "Invalid Argument - Terminating.." << endl;
//            return -1;
//        } else{
//            cout << "Using ppzsknark in the generic group model [Gro16]." << endl;
//        }
//        inputStartIndex = 1;
//    }
    const char *task_ = (env)->GetStringUTFChars(task, NULL);
    const char *mode_ = (env)->GetStringUTFChars(mode, NULL);

    char path1[100] = "../../java/makeinputs/";
    char path2[100] = "../makeinputs/";
    char* arithpath = strcat(strcat(path1, task_),".arith");
    char* inpath = strcat(strcat(path2, task_),".in");
    // Read the circuit, evaluate, and translate constraints
    LOGD("%s", arithpath);
    LOGD("%s", inpath);
    CircuitReader reader(arithpath, inpath, pb);
//    r1cs_constraint_system<FieldT> cs = get_constraint_system_from_gadgetlib2(
//            *pb);
//    const r1cs_variable_assignment<FieldT> full_assignment =
//            get_variable_assignment_from_gadgetlib2(*pb);
//    cs.primary_input_size = reader.getNumInputs() + reader.getNumOutputs();
//    cs.auxiliary_input_size = full_assignment.size() - cs.num_inputs();
//
//    // extract primary and auxiliary input
//    const r1cs_primary_input<FieldT> primary_input(full_assignment.begin(),
//                                                   full_assignment.begin() + cs.num_inputs());
//    const r1cs_auxiliary_input<FieldT> auxiliary_input(
//            full_assignment.begin() + cs.num_inputs(), full_assignment.end());
//
//
//    // only print the circuit output values if both flags MONTGOMERY and BINARY outputs are off (see CMakeLists file)
//    // In the default case, these flags should be ON for faster performance.
//
//#if !defined(MONTGOMERY_OUTPUT) && !defined(OUTPUT_BINARY)
//    cout << endl << "Printing output assignment in readable format:: " << endl;
//    std::vector<Wire> outputList = reader.getOutputWireIds();
//    int start = reader.getNumInputs();
//    int end = reader.getNumInputs() +reader.getNumOutputs();
//    for (int i = start ; i < end; i++) {
//        cout << "[output]" << " Value of Wire # " << outputList[i-reader.getNumInputs()] << " :: ";
//        cout << primary_input[i];
//        cout << endl;
//    }
//    cout << endl;
//#endif
//
//    //assert(cs.is_valid());
//
//    // removed cs.is_valid() check due to a suspected (off by 1) issue in a newly added check in their method.
//    // A follow-up will be added.
//    if(!cs.is_satisfied(primary_input, auxiliary_input)){
//        cout << "The constraint system is  not satisifed by the value assignment - Terminating." << endl;
//        LOGD("1194");
//    }
//    r1cs_example<FieldT> example(cs, primary_input, auxiliary_input);
//    const bool test_serialization = false;
//    bool successBit = false;
//    //string name = argv[2];
//    char *name1;
//    // strncpy(name1, argv[2], strlen(argv[2])-3);
//    name1 = strtok(argv[2], ".");
//    cout << argv[3] << endl;
//    name1[strlen(name1)] = '\0';
//    // cout << name1 << endl;
//    // cout << "voterno : " << argv[4] << endl;
//    string name = name1;
//    // cout << name << endl;
//    if(strcmp(argv[3], "setup") == 0)
//    {
//        libsnark::run_r1cs_gg_ppzksnark_setup<libsnark::default_r1cs_gg_ppzksnark_pp>(example, test_serialization, name);
//
//        return 0;
//    }
//    else if(strcmp(argv[3], "verify") == 0)
//    {
//        if(argc == 5) {
//
//            successBit = libsnark::run_r1cs_gg_ppzksnark_verify<libff::default_ec_pp>(example, test_serialization, name, argv[4]);
//
//        } else {
//            // The following code makes use of the observation that
//            // libsnark::default_r1cs_gg_ppzksnark_pp is the same as libff::default_ec_pp (see r1cs_gg_ppzksnark_pp.hpp)
//            // otherwise, the following code won't work properly, as GadgetLib2 is hardcoded to use libff::default_ec_pp.
//            successBit = libsnark::run_r1cs_gg_ppzksnark_verify<libsnark::default_r1cs_gg_ppzksnark_pp>(
//                    example, test_serialization, name, argv[4]);
//        }
//
//        if(!successBit){
//            cout << "Problem occurred while running the ppzksnark algorithms .. " << endl;
//            return 0;
//        }
//        return 0;
//    }
//    else if (strcmp(argv[3], "run") == 0)
//    {
//        if(argc == 5) {
//
//            libsnark::run_r1cs_gg_ppzksnark<libff::default_ec_pp>(example, test_serialization, name, argv[4]);
//
//        } else {
//            // The following code makes use of the observation that
//            // libsnark::default_r1cs_gg_ppzksnark_pp is the same as libff::default_ec_pp (see r1cs_gg_ppzksnark_pp.hpp)
//            // otherwise, the following code won't work properly, as GadgetLib2 is hardcoded to use libff::default_ec_pp.
//            libsnark::run_r1cs_gg_ppzksnark<libsnark::default_r1cs_gg_ppzksnark_pp>(
//                    example, test_serialization, name, argv[4]);
//        }
//
//
//        return 0;
//    }
//    else if(strcmp(argv[3], "all") == 0)
//    {
//        libsnark::run_r1cs_gg_ppzksnark_setup<libsnark::default_r1cs_gg_ppzksnark_pp>(example, test_serialization, name);
//
//        if(argc == 5) {
//
//            libsnark::run_r1cs_gg_ppzksnark<libff::default_ec_pp>(example, test_serialization, name, argv[4]);
//
//        } else {
//            // The following code makes use of the observation that
//            // libsnark::default_r1cs_gg_ppzksnark_pp is the same as libff::default_ec_pp (see r1cs_gg_ppzksnark_pp.hpp)
//            // otherwise, the following code won't work properly, as GadgetLib2 is hardcoded to use libff::default_ec_pp.
//            libsnark::run_r1cs_gg_ppzksnark<libsnark::default_r1cs_gg_ppzksnark_pp>(
//                    example, test_serialization, name, argv[4]);
//        }
//
//        if(argc == 5) {
//
//            successBit = libsnark::run_r1cs_gg_ppzksnark_verify<libff::default_ec_pp>(example, test_serialization, name, argv[4]);
//
//        } else {
//            // The following code makes use of the observation that
//            // libsnark::default_r1cs_gg_ppzksnark_pp is the same as libff::default_ec_pp (see r1cs_gg_ppzksnark_pp.hpp)
//            // otherwise, the following code won't work properly, as GadgetLib2 is hardcoded to use libff::default_ec_pp.
//            successBit = libsnark::run_r1cs_gg_ppzksnark_verify<libsnark::default_r1cs_gg_ppzksnark_pp>(
//                    example, test_serialization, name, argv[4]);
//        }
//
//        if(!successBit){
//            cout << "Problem occurred while running the ppzksnark algorithms .. " << endl;
//            return 0;
//        }

    return env->NewStringUTF("1");
}