import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IPaciente } from '@/shared/model/paciente.model';

import PacienteService from './paciente.service';

export default defineComponent({
  name: 'PacienteDetails',
  setup() {
    const pacienteService = inject('pacienteService', () => new PacienteService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const paciente: Ref<IPaciente> = ref({});

    const retrievePaciente = async pacienteId => {
      try {
        const res = await pacienteService().find(pacienteId);
        paciente.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.pacienteId) {
      retrievePaciente(route.params.pacienteId);
    }

    return {
      alertService,
      paciente,

      ...dataUtils,

      previousState,
    };
  },
});
