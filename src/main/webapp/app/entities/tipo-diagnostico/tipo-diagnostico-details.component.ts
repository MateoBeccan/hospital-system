import { type Ref, defineComponent, inject, ref } from 'vue';
import { useRoute, useRouter } from 'vue-router';

import { useAlertService } from '@/shared/alert/alert.service';
import { type ITipoDiagnostico } from '@/shared/model/tipo-diagnostico.model';

import TipoDiagnosticoService from './tipo-diagnostico.service';

export default defineComponent({
  name: 'TipoDiagnosticoDetails',
  setup() {
    const tipoDiagnosticoService = inject('tipoDiagnosticoService', () => new TipoDiagnosticoService());
    const alertService = inject('alertService', () => useAlertService(), true);

    const route = useRoute();
    const router = useRouter();

    const previousState = () => router.go(-1);
    const tipoDiagnostico: Ref<ITipoDiagnostico> = ref({});

    const retrieveTipoDiagnostico = async tipoDiagnosticoId => {
      try {
        const res = await tipoDiagnosticoService().find(tipoDiagnosticoId);
        tipoDiagnostico.value = res;
      } catch (error) {
        alertService.showHttpError(error.response);
      }
    };

    if (route.params?.tipoDiagnosticoId) {
      retrieveTipoDiagnostico(route.params.tipoDiagnosticoId);
    }

    return {
      alertService,
      tipoDiagnostico,

      previousState,
    };
  },
});
