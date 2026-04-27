import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import useDataUtils from '@/shared/data/data-utils.service';
import { type IDiagnostico } from '@/shared/model/diagnostico.model';

import DiagnosticoService from './diagnostico.service';

export default defineComponent({
  name: 'DiagnosticoDetails',
  setup() {
    const diagnosticoService = inject('diagnosticoService', () => new DiagnosticoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const dataUtils = useDataUtils();

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const diagnostico: Ref<IDiagnostico> = ref({});

    const retrieveDiagnostico = async diagnosticoId => {
      try {
        const res = await diagnosticoService().find(diagnosticoId);
        diagnostico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.diagnosticoId) {
      retrieveDiagnostico(route.params.diagnosticoId);
    }

    return {
      alertService,
      diagnostico,

      ...dataUtils,

      previousState,
    };
  },
});
